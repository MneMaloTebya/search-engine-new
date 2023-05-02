package searchengine.model;

import org.springframework.data.jpa.repository.JpaRepository;
import searchengine.model.entity.LemmaEntity;

import java.util.List;
import java.util.Optional;

public interface LemmaRepository extends JpaRepository<LemmaEntity, Integer> {
    Optional<LemmaEntity> findByLemma(String lemma);
    void deleteAllBySiteId(int id);
    int countBySiteId(int id);
    List<LemmaEntity> findAllBySiteId(int id);
}
