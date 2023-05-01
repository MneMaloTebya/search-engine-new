package searchengine.services.lemmatizer;

import searchengine.model.entity.IndexEntity;

import java.util.Optional;

public interface IndexService {
    IndexEntity save(IndexEntity entity);
    void deleteAllByPageId(int id);
    Optional<IndexEntity> findByPageId(int id);
}
