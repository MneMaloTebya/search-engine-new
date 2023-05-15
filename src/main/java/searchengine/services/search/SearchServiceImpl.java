package searchengine.services.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import searchengine.dto.search_resp.SearchResponse;
import searchengine.model.entity.LemmaEntity;
import searchengine.model.entity.SiteEntity;
import searchengine.services.lemmatizer.LemmaFinder;
import searchengine.services.lemmatizer.LemmaService;
import searchengine.services.site.SiteService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class SearchServiceImpl implements SearchService {

    private final LemmaService lemmaService;
    private final SiteService siteService;

    @Autowired
    public SearchServiceImpl(LemmaService lemmaService, SiteService siteService) {
        this.lemmaService = lemmaService;
        this.siteService = siteService;
    }

    @Override
    public SearchResponse search(String query, String site, int offset, int limit) {



        return null;
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

    private Set<String> replaceQuery(String query) {
        try {
            LemmaFinder lemmaFinder = LemmaFinder.getInstance();
            return lemmaFinder.getLemmaSet(query);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
