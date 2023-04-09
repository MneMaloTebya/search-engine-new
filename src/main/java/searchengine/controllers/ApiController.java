package searchengine.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import searchengine.dto.indexing.IndexingResponse;
import searchengine.dto.statistics.StatisticsResponse;
import searchengine.services.indexing.IndexService;
import searchengine.services.statistic.StatisticsService;

@RestController
@RequestMapping(value = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
public class ApiController {


    private final StatisticsService statisticsService;
    private final IndexService indexService;

    @Autowired
    public ApiController(StatisticsService statisticsService, IndexService indexService) {
        this.statisticsService = statisticsService;
        this.indexService = indexService;
    }

    @GetMapping("/statistics")
    public ResponseEntity<StatisticsResponse> statistics() {
        return ResponseEntity.ok(statisticsService.getStatistics());
    }

    @GetMapping(value = "/startIndexing", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<IndexingResponse> startIndexing() {
        return ResponseEntity.ok(indexService.startIndex());
    }

    @GetMapping("/stopIndexing")
    public ResponseEntity<IndexingResponse> stopIndexing() {
        return ResponseEntity.ok(indexService.stopIndex());
    }

    @PostMapping("/indexPage")
    public ResponseEntity<IndexingResponse> indexPage(@RequestParam String url) {
        return ResponseEntity.ok(indexService.indexPage(url));
    }
}
