package searchengine.services.page_parser;

import searchengine.model.domain.SiteDto;
import searchengine.services.index_assistant.DataInserterService;
import searchengine.services.my_assistant.MyConnector;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ForkJoinPool;

public class ParserThread implements Callable<Boolean> {

    private final PageParserService pageParserService;
    private final RecursiveTask recursiveTask;
    private final MyConnector myConnector;

    public ParserThread(DataInserterService dataInserterService, SiteDto dto, String currentUrl, MyConnector myConnector) {
        this.myConnector = myConnector;
        pageParserService = new PageParserServiceImpl(dataInserterService, this.myConnector);
        Set<String> siteDataMainPage = pageParserService.parsing(currentUrl, dto);
        recursiveTask = new RecursiveTask(siteDataMainPage, pageParserService, dto);
    }



    @Override
    public Boolean call() {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        forkJoinPool.invoke(recursiveTask);
        return true;
    }
}
