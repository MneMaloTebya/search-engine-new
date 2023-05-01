package searchengine.services.indexing;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jetbrains.annotations.NotNull;
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
import searchengine.services.my_assistant.TaskContext;
import searchengine.services.page.PageService;
import searchengine.services.page_parser.PageParserService;
import searchengine.services.page_parser.PageValidator;
import searchengine.services.page_parser.RecursiveTask;
import searchengine.services.site.SiteService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadPoolExecutor;

@Service
public class IndexServiceImpl implements IndexService {

    private final SitesList sitesList;
    private final SiteService siteService;
    private final PageService pageService;
    private final PageParserService pageParserService;

    private final TaskContext taskContext = new TaskContext();
    private final ForkJoinPool pool = new ForkJoinPool();

    private static final Log log = LogFactory.getLog(IndexServiceImpl.class);

    @Autowired
    public IndexServiceImpl(SitesList sitesList, SiteService siteService, PageService pageService, PageParserService pageParserService) {
        this.sitesList = sitesList;
        this.siteService = siteService;
        this.pageService = pageService;
        this.pageParserService = pageParserService;
    }

    @Override
    public IndexingResponse startIndex() {
        PageValidator.removeUrls(PageValidator.getUrlsSite());

        if (pool.getActiveThreadCount() > 1) {
            IndexingResponse response = new ErrorIndexingResponse();
            response.setResult(false);
            return response;
        }

        sitesList.getSites().forEach(this::startSiteIndex);
        sitesList.getSites().forEach(siteService::setIndexedStatus);
        IndexingResponse response = new CorrectIndexingResponse();
        response.setResult(true);
        return response;
    }

    private void startSiteIndex(@NotNull Site site) {
        siteService.deleteByUrl(site.getUrl());
        SiteEntity entity = siteService.save(site, StatusType.INDEXING);
        SiteDto dto = SiteDtoMapper.toDomain(entity);
        pool.invoke(init(dto.getUrl(), dto));
    }

    @Override
    public IndexingResponse stopIndex() {
        IndexingResponse response = null;
        if (pool.getActiveThreadCount() == 0) {
            response = new ErrorIndexingResponse("Индексация не запущена");
        } else {
//            taskContext.setPoolIsStopped(true);
            taskContext.stopRecursiveTask();
            for (Site site : sitesList.getSites()) {
                siteService.setFailedStatus(site, "Индексация прервана");
                response = new CorrectIndexingResponse();
            }
        }
        // TODO: 27.04.2023 мы ждем в потоке, пока все таски в листе не будут isCanceled is true

        return response;
    }

    @Override
    public IndexingResponse indexPage(String url) {
        IndexingResponse response = null;
        if (sitesList.urlIsLocatedConfig(url)) {
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

    private @NotNull RecursiveTask init(String currentUrl, SiteDto dto) {
        Set<String> siteDataMainPage = pageParserService.parsing(currentUrl, dto);
        return new RecursiveTask(siteDataMainPage, pageParserService, dto, taskContext);
    }
}
