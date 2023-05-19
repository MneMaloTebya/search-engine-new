package searchengine.services.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import searchengine.dto.search_resp.SearchResponse;
import searchengine.model.domain.PageDto;
import searchengine.model.domain.PageDtoMapper;
import searchengine.model.entity.IndexEntity;
import searchengine.model.entity.LemmaEntity;
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

    private List<LemmaEntity> collectLemmas(String url, String query) {
        Set<String> lemmasQuery = replaceQuery(query);
        List<LemmaEntity> lemmas;
        if (url == null) {
            lemmas = lemmaService.findAll();
            dropPopular(lemmasQuery, lemmas);
        } else {
            SiteEntity siteEntity = siteService.findByUrl(url).orElse(null); // TODO: 19.05.2023 проверить корректность урла на входе
            lemmas = lemmaService.findAllBySiteId(siteEntity.getId());
            dropPopular(lemmasQuery, lemmas);
        }
        return lemmas;
    }

    private void dropPopular(Set<String> lemmasQuery, List<LemmaEntity> lemmas) {
        int lemmasCount = lemmas.size();
        for (String lemma : lemmasQuery) {
            Optional<LemmaEntity> optionalLemma = lemmaService.findByLemma(lemma);
            if (optionalLemma.isPresent()) {
                LemmaEntity lemmaEntity = optionalLemma.get();
                int frequency = lemmaEntity.getFrequency();
                int popular = frequency / lemmasCount * 100;
                if (popular >= 5) { // TODO: 19.05.2023 тестовое значение
                    lemmas.remove(lemmaEntity);
                }
            }
        }
    }

    private List<LemmaEntity> sortLemmas(List<LemmaEntity> lemmasBySite) {
        return lemmasBySite.stream().sorted(Comparator.comparing(LemmaEntity::getFrequency).reversed()).toList();
    }

    private void filterPage(String site, String query) {

        SiteEntity siteEntity = siteService.findByUrl(site).orElse(null);
        List<LemmaEntity> sortedLemmas = sortLemmas(collectLemmas(siteEntity.getUrl(), query));

        for (LemmaEntity lemma : sortedLemmas) {
            List<IndexEntity> indexes = indexService.findAllByLemmaId(lemma.getId());
            for (IndexEntity index : indexes) {
                PageDto pageDto = PageDtoMapper.toDomain(pageService.findById(index.getPageId()).orElse(null));

            }
        }

    }
}
