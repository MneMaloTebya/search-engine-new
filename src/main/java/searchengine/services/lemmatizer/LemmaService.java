package searchengine.services.lemmatizer;

import searchengine.model.entity.LemmaEntity;

import java.util.Optional;

public interface LemmaService {
    Optional<LemmaEntity> findBySiteId(int id);
    Optional<LemmaEntity> findByLemma(String lemma);
    void deleteAllBySiteId(int id);
    LemmaEntity save(LemmaEntity lemma);
    int countBySiteId(int id);
}
