package searchengine.services.page_parser;

import searchengine.model.domain.SiteDto;

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
       List<RecursiveTask> taskList = new ArrayList<>();
       Set<String> urls = new HashSet<>();
       for (String recursiveLink : siteUrls) {
           Set<String> urlsSet = pageParserService.parsing(recursiveLink, dto);
           RecursiveTask task = new RecursiveTask(urlsSet, pageParserService, dto);
           task.fork();
           taskList.add(task);
       }
       for (RecursiveTask task : taskList) {
           urls.addAll(task.join());
       }
        return urls;
    }
}
