package searchengine.services.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import searchengine.dto.search_resp.SearchResponse;
import searchengine.model.entity.IndexEntity;
import searchengine.model.entity.LemmaEntity;
import searchengine.model.entity.PageEntity;
import searchengine.model.entity.SiteEntity;
import searchengine.services.lemmatizer.IndexService;
import searchengine.services.lemmatizer.LemmaFinder;
import searchengine.services.lemmatizer.LemmaService;
import searchengine.services.page.PageService;
import searchengine.services.site.SiteService;

import java.io.IOException;
import java.util.*;

@Service
public class SearchServiceImpl implements SearchService {

    private final LemmaService lemmaService;
    private final SiteService siteService;
    private final IndexService indexService;
    private final PageService pageService;

    @Autowired
    public SearchServiceImpl(LemmaService lemmaService, SiteService siteService, IndexService indexService, PageService pageService) {
        this.lemmaService = lemmaService;
        this.siteService = siteService;
        this.indexService = indexService;
        this.pageService = pageService;
    }

    @Override
    public SearchResponse search(String query, String site, int offset, int limit) {


        return null;
    }

    private Set<String> replaceQuery(String query) {
        try {
            LemmaFinder lemmaFinder = LemmaFinder.getInstance();
            return lemmaFinder.getLemmaSet(query);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<LemmaEntity> dropPopularLemmas(String url, String query) {
        Set<String> lemmasQuery = replaceQuery(query);
        SiteEntity siteEntity = siteService.findByUrl(url).orElse(null);
        List<LemmaEntity> lemmasBySite = lemmaService.findAllBySiteId(siteEntity.getId());
        int countLemmasSite = lemmasBySite.size();
        for (String lemma : lemmasQuery) {
            Optional<LemmaEntity> optionalLemma = lemmaService.findByLemma(lemma);
            if (optionalLemma.isPresent()) {
                LemmaEntity lemmaEntity = optionalLemma.get();
                int frequency = lemmaEntity.getFrequency();
                int popular = frequency / countLemmasSite * 100;
                if (popular >= 5) {
                    lemmasBySite.remove(lemmaEntity);
                }
            }
        }
        return lemmasBySite;
    }

    private List<LemmaEntity> sortLemmas(List<LemmaEntity> lemmasBySite) {
        return lemmasBySite.stream().sorted(Comparator.comparing(LemmaEntity::getFrequency).reversed()).toList();
    }

    private Set<PageEntity> filterPage(String site, String query) {
        Set<PageEntity> sortedPages = new TreeSet<>();
        SiteEntity siteEntity = siteService.findByUrl(site).orElse(null);
        List<LemmaEntity> sortedLemmas = sortLemmas(dropPopularLemmas(siteEntity.getUrl(), query));

        for (LemmaEntity lemma : sortedLemmas) {
            List<IndexEntity> indexes = indexService.findAllByLemmaId(lemma.getId());
            for (IndexEntity index : indexes) {
                PageEntity page = pageService.findById(index.getPageId()).orElse(null);
                sortedPages.add(page);
            }
        }
        return sortedPages;
    }
}
