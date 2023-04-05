package searchengine.model;

import org.springframework.data.jpa.repository.JpaRepository;
import searchengine.model.entity.PageEntity;

import java.util.Optional;

public interface PageRepository extends JpaRepository<PageEntity, Integer> {
    void deleteAllBySiteId(int id);
    Optional<PageEntity> findPageEntityByPathAndSiteId(String url, int siteId);
}
