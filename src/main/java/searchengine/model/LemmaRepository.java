package searchengine.model;

import org.springframework.data.jpa.repository.JpaRepository;
import searchengine.model.entity.LemmaEntity;

public interface LemmaRepository extends JpaRepository<LemmaEntity, Integer> {
}
