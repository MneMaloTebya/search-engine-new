package searchengine.services.page_parser;

import searchengine.dto.indexing.ThreadResponse;
import searchengine.model.domain.SiteDto;
import searchengine.services.index_assistant.DataInserterService;
import searchengine.services.my_assistant.MyConnector;

import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ForkJoinPool;

public class ParserThread implements Callable<ThreadResponse> {

    private final PageParserService pageParserService;
    private final RecursiveTaskService recursiveTaskService;
    private final MyConnector myConnector;

    public ParserThread(DataInserterService dataInserterService, SiteDto dto, String currentUrl, MyConnector myConnector) {
        this.myConnector = myConnector;
        pageParserService = new PageParserServiceImpl(dataInserterService, this.myConnector);
        Set<String> siteDataMainPage = pageParserService.parsing(currentUrl, dto);
        recursiveTaskService = new RecursiveTaskService(siteDataMainPage, pageParserService, dto);
    }

    @Override
    public ThreadResponse call() {
        ThreadResponse response = new ThreadResponse();
        ForkJoinPool.commonPool().invoke(recursiveTaskService);
        ForkJoinPool.commonPool().shutdownNow();
        response.setResponse(ForkJoinPool.commonPool().isShutdown());
        return response;
    }
}
