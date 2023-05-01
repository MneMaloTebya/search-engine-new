package searchengine.model;

import org.springframework.data.jpa.repository.JpaRepository;
import searchengine.model.entity.LemmaEntity;

import java.util.Optional;

public interface LemmaRepository extends JpaRepository<LemmaEntity, Integer> {
    Optional<LemmaEntity> findBySiteId(int id);
    Optional<LemmaEntity> findByLemma(String lemma);
    void deleteAllBySiteId(int id);
    int countBySiteId(int id);
}
