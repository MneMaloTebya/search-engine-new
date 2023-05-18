package searchengine.services.lemmatizer;

import searchengine.model.entity.IndexEntity;

import java.util.List;

public interface IndexService {
    IndexEntity save(IndexEntity entity);
    void deleteAllByPageId(int id);
    List<IndexEntity> findAllByPageId(int id);
    List<IndexEntity> findAllByLemmaId(int id);
}
