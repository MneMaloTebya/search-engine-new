package searchengine.services.page_parser;

import searchengine.model.domain.SiteDto;
import searchengine.services.my_assistant.TaskContext;

import java.util.*;

public class RecursiveTask extends java.util.concurrent.RecursiveTask<Set<String>> {

    private final Set<String> siteUrls;
    private final PageParserService pageParserService;
    private final SiteDto dto;

    public RecursiveTask(Set<String> siteUrls, PageParserService pageParserService, SiteDto dto) {
        this.siteUrls = siteUrls;
        this.pageParserService = pageParserService;
        this.dto = dto;
    }

    @Override
    public Set<String> compute() {
       Set<String> urls = new HashSet<>();
       for (String recursiveLink : siteUrls) {
           Set<String> urlsSet = pageParserService.parsing(recursiveLink, dto);
           RecursiveTask task = new RecursiveTask(urlsSet, pageParserService, dto);
           TaskContext.addTask(task);
           task.fork();
       }
       for (RecursiveTask task : TaskContext.getTasks()) {
           urls.addAll(task.join());
       }
        return urls;
    }
}
