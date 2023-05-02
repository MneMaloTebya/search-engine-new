package searchengine.services.statistic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import searchengine.config.Site;
import searchengine.config.SitesList;
import searchengine.dto.statistics.DetailedStatisticsItem;
import searchengine.dto.statistics.StatisticsData;
import searchengine.dto.statistics.StatisticsResponse;
import searchengine.dto.statistics.TotalStatistics;
import searchengine.model.entity.SiteEntity;
import searchengine.services.lemmatizer.LemmaService;
import searchengine.services.page.PageService;
import searchengine.services.site.SiteService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    private final SitesList sites;
    private final PageService pageService;
    private final SiteService siteService;
    private final LemmaService lemmaService;

    @Autowired
    public StatisticsServiceImpl(SitesList sites, PageService pageService, SiteService siteService, LemmaService lemmaService) {
        this.sites = sites;
        this.pageService = pageService;
        this.siteService = siteService;
        this.lemmaService = lemmaService;
    }

    @Override
    public StatisticsResponse getStatistics() {
        TotalStatistics total = new TotalStatistics();
        total.setSites(sites.getSites().size());
        total.setIndexing(true);
        List<DetailedStatisticsItem> detailed = new ArrayList<>();
        List<Site> sitesList = sites.getSites();
        for (Site site : sitesList) {
            DetailedStatisticsItem item = new DetailedStatisticsItem();
            item.setName(site.getName());
            item.setUrl(site.getUrl());
            Optional<SiteEntity> optionalSite = siteService.findByUrl(site.getUrl());
            if (optionalSite.isPresent()) {
                SiteEntity siteEntity = optionalSite.get();
                int pages = pageService.countBySiteId(siteEntity.getId());
                int lemmas = lemmaService.countBySiteId(siteEntity.getId());
                item.setPages(pages);
                item.setLemmas(lemmas);
                item.setStatus(siteEntity.getStatusType().toString());
                if (siteEntity.getLastError() == null) {
                    item.setError("-");
                } else {
                    item.setError(siteEntity.getLastError());
                }
                item.setStatusTime(siteEntity.getStatusTime().getTime());
                total.setPages(total.getPages() + pages);
                total.setLemmas(total.getLemmas() + lemmas);
                detailed.add(item);
            }
        }
        StatisticsResponse response = new StatisticsResponse();
        StatisticsData data = new StatisticsData();
        data.setTotal(total);
        data.setDetailed(detailed);
        response.setStatistics(data);
        response.setResult(true);
        return response;
    }
}
