package searchengine.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import searchengine.dto.indx_resp.IndexingResponse;
import searchengine.dto.search_resp.SearchResponse;
import searchengine.dto.statistics.StatisticsResponse;
import searchengine.services.indexing.IndexingService;
import searchengine.services.search.SearchService;
import searchengine.services.statistic.StatisticsService;

@RestController
@RequestMapping(value = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
public class ApiController {


    private final StatisticsService statisticsService;
    private final IndexingService indexingService;
    private final SearchService searchService;

    @Autowired
    public ApiController(StatisticsService statisticsService, IndexingService indexingService, SearchService searchService) {
        this.statisticsService = statisticsService;
        this.indexingService = indexingService;
        this.searchService = searchService;
    }

    @GetMapping("/statistics")
    public ResponseEntity<StatisticsResponse> statistics() {
        return ResponseEntity.ok(statisticsService.getStatistics());
    }

    @GetMapping(value = "/startIndexing", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<IndexingResponse> startIndexing() {
        return ResponseEntity.ok(indexingService.startIndex());
    }

    @GetMapping("/stopIndexing")
    public ResponseEntity<IndexingResponse> stopIndexing() {
        return ResponseEntity.ok(indexingService.stopIndex());
    }

    @PostMapping("/indexPage")
    public ResponseEntity<IndexingResponse> indexPage(@RequestParam(name = "url") String url) {
        return ResponseEntity.ok(indexingService.indexPage(url));
    }

    @GetMapping("/search")
    public ResponseEntity<SearchResponse> search(@RequestParam String query,
                                                 @RequestParam String site,
                                                 @RequestParam int offset,
                                                 @RequestParam int limit) {
        return ResponseEntity.ok(searchService.search(query, site, offset, limit));
    }
}
