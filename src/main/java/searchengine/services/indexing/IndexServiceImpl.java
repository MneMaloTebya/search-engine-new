package searchengine.services.indexing;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import searchengine.config.Site;
import searchengine.config.SitesList;
import searchengine.dto.StatusType;
import searchengine.dto.indexing.ErrorIndexingResponse;
import searchengine.dto.indexing.CorrectIndexingResponse;
import searchengine.dto.indexing.IndexingResponse;
import searchengine.model.domain.SiteDto;
import searchengine.model.domain.SiteDtoMapper;
import searchengine.model.entity.PageEntity;
import searchengine.model.entity.SiteEntity;
import searchengine.services.index_assistant.DataInserterService;
import searchengine.services.my_assistant.MyConnector;
import searchengine.services.my_assistant.TaskContext;
import searchengine.services.page.PageService;
import searchengine.services.page_parser.PageValidator;
import searchengine.services.page_parser.ParserThread;
import searchengine.services.site.SiteService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.*;

@Service
public class IndexServiceImpl implements IndexService {

    private final SitesList sitesList;
    private final SiteService siteService;
    private final PageService pageService;
    private final DataInserterService dataInserterService;
    private final MyConnector myConnector;
    private final ThreadPoolExecutor executor;
    private final List<Future> futures = new ArrayList<>();
    private static final Log log = LogFactory.getLog(IndexServiceImpl.class);


    @Autowired
    public IndexServiceImpl(SitesList sitesList, SiteService siteService, PageService pageService, DataInserterService dataInserterService, MyConnector myConnector) {
        this.sitesList = sitesList;
        this.siteService = siteService;
        this.pageService = pageService;
        this.dataInserterService = dataInserterService;
        this.myConnector = myConnector;
        this.executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(sitesList.getSites().size());
    }

    @Override
    public IndexingResponse startIndex() {
        PageValidator.removeUrls(PageValidator.getUrlsSite());
        TaskContext.removeAll();

        if (executor.getActiveCount() > 0) {
            IndexingResponse response = new ErrorIndexingResponse();
            response.setResult(false);
            return response;
        }

        sitesList.getSites().forEach(this::startSiteIndex);
        executor.shutdown();
        System.out.println(executor.isShutdown());

        IndexingResponse response = new CorrectIndexingResponse();
        response.setResult(true);
        return response;
    }

    private void startSiteIndex(Site site) {
        siteService.deleteByUrl(site.getUrl());
        SiteDto dto = SiteDtoMapper.toDomain(siteService.save(site, StatusType.INDEXING));
        ParserThread parserThread = new ParserThread(dataInserterService, dto, site.getUrl(), myConnector);
        futures.add(executor.submit(parserThread));
    }

    @Override
    public IndexingResponse stopIndex() {
        IndexingResponse response = null;
        if (executor.getActiveCount() == 0) {
            stopRecursiveTask();
            response = new ErrorIndexingResponse("Индексация не запущена");
        } else {
            stopRecursiveTask();
            stopExecutor();
            for (Site site : sitesList.getSites()) {
                Optional<SiteEntity> optionalSite = siteService.findByUrl(site.getUrl());
                if (optionalSite.isPresent()) {
                    SiteEntity entity = optionalSite.get();
                    if (entity.getStatusType().equals(StatusType.INDEXING)) {
                        entity.setLastError("Индексация прервана");
                        entity.setStatusType(StatusType.FAILED);
                        siteService.changeStatus(entity, StatusType.FAILED);
                        response = new CorrectIndexingResponse();
                    }
                }
            }
        }
        return response;
    }

    @Override
    public IndexingResponse indexPage(String url) {
        IndexingResponse response = null;
        if (urlIsLocatedConfig(url)) {
            response = new ErrorIndexingResponse("Данная страница находится за пределами сайтов, указанных в конфигурационном файле");
        }

        Optional<SiteEntity> optionalSite = siteService.findByUrlContains(url);
        Optional<PageEntity> optionalPage = pageService
                .findPageEntityByPathAndSiteId(PageValidator.getPathFromUrl(url), optionalSite.get().getId());
        if (optionalPage.isPresent()) {
            // TODO: 09.04.2023 удалить существующие индексы и леммы
        } else {
            // TODO: 09.04.2023 логика добавления новой страницы ее индексов и лемм

            response = new CorrectIndexingResponse();
            response.setResult(true);
        }
        return response;
    }

    private boolean urlIsLocatedConfig(String url) {
        List<String> siteUrl = new ArrayList<>();
        for (Site site : sitesList.getSites()) {
            siteUrl.add(site.getUrl());
        }
        for (String site : siteUrl) {
            if (site.contains(PageValidator.getHostFromUrl(url))) {
                return true;
            }
        }
        return false;
    }

    private void stopRecursiveTask() {
        TaskContext.getTasks().forEach(t -> {
            t.cancel(true);
            log.info(t + " прервана: " + t.isDone());
        });
    }

    private void stopExecutor() {
        futures.forEach(f -> {
            f.cancel(true);
            log.info("Фьючер завершил работу: " + f.isDone());
        });
        executor.shutdownNow();
    }
}
