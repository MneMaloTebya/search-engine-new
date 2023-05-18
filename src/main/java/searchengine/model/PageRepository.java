package searchengine.model;

import org.springframework.data.jpa.repository.JpaRepository;
import searchengine.model.entity.PageEntity;

import java.util.List;
import java.util.Optional;

public interface PageRepository extends JpaRepository<PageEntity, Integer> {
    void deleteAllBySiteId(int id);
    Optional<PageEntity> findPageEntityByPathAndSiteId(String url, int siteId);
    void deleteByPathAndSiteId(String path, int id);
    int countBySiteId(int id);
    List<PageEntity> findAllBySiteId(int id);
}
