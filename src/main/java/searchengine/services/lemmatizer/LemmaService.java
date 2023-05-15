package searchengine.services.lemmatizer;

import searchengine.model.entity.LemmaEntity;

import java.util.List;
import java.util.Optional;

public interface LemmaService {
    List<LemmaEntity> findAllBySiteId(int id);
    Optional<LemmaEntity> findByLemma(String lemma);
    void deleteAllBySiteId(int id);
    LemmaEntity save(LemmaEntity lemma);
    int countBySiteId(int id);
    List<LemmaEntity> findAll();
    List<LemmaEntity> findAllByUrl(String url);
}
