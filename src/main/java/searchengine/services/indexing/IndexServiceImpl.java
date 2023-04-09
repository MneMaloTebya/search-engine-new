package searchengine.services.indexing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import searchengine.config.Site;
import searchengine.config.SitesList;
import searchengine.dto.StatusType;
import searchengine.dto.indexing.ErrorIndexingResponse;
import searchengine.dto.indexing.CorrectIndexingResponse;
import searchengine.dto.indexing.IndexingResponse;
import searchengine.dto.indexing.ThreadResponse;
import searchengine.model.domain.SiteDto;
import searchengine.model.domain.SiteDtoMapper;
import searchengine.model.entity.SiteEntity;
import searchengine.services.index_assistant.DataInserterService;
import searchengine.services.my_assistant.MyConnector;
import searchengine.services.page_parser.PageValidator;
import searchengine.services.page_parser.ParserThread;
import searchengine.services.site.SiteService;

import java.util.Optional;
import java.util.concurrent.*;

@Service
public class IndexServiceImpl implements IndexService {

    private final SitesList sitesList;
    private final SiteService siteService;
    private final ThreadPoolExecutor executor;
    private final DataInserterService dataInserterService;
    private final MyConnector myConnector;

    @Autowired
    public IndexServiceImpl(SitesList sitesList, SiteService siteService,  DataInserterService dataInserterService, MyConnector myConnector) {
        this.sitesList = sitesList;
        this.siteService = siteService;
        this.executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(sitesList.getSites().size());
        this.dataInserterService = dataInserterService;
        this.myConnector = myConnector;
    }

    @Override
    public IndexingResponse startIndex() {
        PageValidator.removeUrls(PageValidator.getUrlsSite());
        if (executor.getActiveCount() > 0) {
            IndexingResponse response = new ErrorIndexingResponse();
            response.setResult(false);
            return response;
        }
        for (Site site : sitesList.getSites()) {
            startSiteIndex(site);
        }
        IndexingResponse response = new CorrectIndexingResponse();
        response.setResult(true);
        return response;
    }

    private void startSiteIndex(Site site) {
        siteService.deleteByUrl(site.getUrl());
        SiteDto dto = SiteDtoMapper.toDomain(siteService.save(site, StatusType.INDEXING));
        //try {
            ParserThread parserThread = new ParserThread(dataInserterService, dto, site.getUrl(), myConnector, executor);
            Future<ThreadResponse> future = executor.submit(parserThread);
            if (future.isDone()) {
                siteService.changeStatus(dto, StatusType.INDEXED);
                //future.cancel(true);
            }
            //siteService.changeStatus(siteEntity, StatusType.INDEXED);
//        } catch (Exception e) {
//            siteEntity.setLastError(e.getMessage());
//            siteService.save(site, StatusType.FAILED);
//        }
    }

    @Override
    public IndexingResponse stopIndex() {
        IndexingResponse response = null;
        if (executor.getActiveCount() == 0) {
            response = new ErrorIndexingResponse("Индексация не запущена");
        } else {
            for (Site site : sitesList.getSites()) {
                Optional<SiteEntity> optionalSite = siteService.findByUrl(site.getUrl());
                if (optionalSite.isPresent()) {
                    SiteEntity entity = optionalSite.get();
                    SiteDto dto = SiteDtoMapper.toDomain(entity);
                    if (entity.getStatusType().equals(StatusType.INDEXING)) {
                        entity.setLastError("Индексация прервана");
                        siteService.changeStatus(dto, StatusType.FAILED);
                        response = new CorrectIndexingResponse();
                        executor.shutdownNow(); // todo потоки не останавливаются
                    }
                }
            }
        }
        return response;
    }
}
