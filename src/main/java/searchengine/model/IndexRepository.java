package searchengine.model;

import org.springframework.data.jpa.repository.JpaRepository;
import searchengine.model.entity.IndexEntity;

import java.util.Optional;

public interface IndexRepository extends JpaRepository<IndexEntity, Integer> {
    Optional<IndexEntity> findByPageId(int id);
    void deleteAllByPageId(int id);
}
