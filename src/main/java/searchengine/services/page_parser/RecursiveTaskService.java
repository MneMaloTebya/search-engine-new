package searchengine.services.page_parser;

import searchengine.model.domain.SiteDto;

import java.util.*;
import java.util.concurrent.RecursiveTask;

public class RecursiveTaskService extends RecursiveTask<Set<String>> {

    private final Set<String> siteUrls;
    private final PageParserService pageParserService;
    private final SiteDto dto;

    public RecursiveTaskService(Set<String> siteUrls, PageParserService pageParserService, SiteDto dto) {
        this.siteUrls = siteUrls;
        this.pageParserService = pageParserService;
        this.dto = dto;
    }

    @Override
    public Set<String> compute() {
       List<RecursiveTaskService> taskList = new ArrayList<>();
       Set<String> urls = new HashSet<>();
       for (String recursiveLink : siteUrls) {
           Set<String> urlsSet = pageParserService.parsing(recursiveLink, dto);
           RecursiveTaskService task = new RecursiveTaskService(urlsSet, pageParserService, dto);
           task.fork();
           taskList.add(task);
       }
       for (RecursiveTaskService task : taskList) {
           urls.addAll(task.join());
       }
        return urls;
    }
}
