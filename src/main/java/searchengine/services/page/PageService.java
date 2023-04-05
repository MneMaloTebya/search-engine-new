package searchengine.services.page;

import searchengine.model.entity.PageEntity;

import java.util.Optional;

public interface PageService {
    Optional<PageEntity> findPageEntityByPathAndSiteId(String path, int siteId);
    PageEntity save(PageEntity page);
}
