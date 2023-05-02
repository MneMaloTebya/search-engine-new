package searchengine.services.search;

import searchengine.services.lemmatizer.LemmaFinder;

import java.io.IOException;
import java.util.Set;

public class SearchServiceImpl implements SearchService {

    private Set<String> replaceQuery(String query) {
        try {
            LemmaFinder lemmaFinder = LemmaFinder.getInstance();
            return lemmaFinder.getLemmaSet(query);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
