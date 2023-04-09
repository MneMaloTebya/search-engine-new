package searchengine.services.page_parser;

import searchengine.dto.indexing.ThreadResponse;
import searchengine.model.domain.SiteDto;
import searchengine.services.index_assistant.DataInserterService;
import searchengine.services.my_assistant.MyConnector;

import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadPoolExecutor;

public class ParserThread implements Callable<ThreadResponse> {

    private final PageParserService pageParserService;
    private final RecursiveTask recursiveTask;
    private final MyConnector myConnector;
    private final ThreadPoolExecutor executor;

    public ParserThread(DataInserterService dataInserterService, SiteDto dto, String currentUrl, MyConnector myConnector, ThreadPoolExecutor executor) {
        this.myConnector = myConnector;
        pageParserService = new PageParserServiceImpl(dataInserterService, this.myConnector);
        this.executor = executor;
        Set<String> siteDataMainPage = pageParserService.parsing(currentUrl, dto);
        recursiveTask = new RecursiveTask(siteDataMainPage, pageParserService, dto);
    }

    @Override
    public ThreadResponse call() {
        ThreadResponse response = new ThreadResponse();
        ForkJoinPool.commonPool().invoke(recursiveTask);
        executor.shutdownNow();
        response.setResponse(ForkJoinPool.commonPool().isShutdown());
        return response;
    }
}
