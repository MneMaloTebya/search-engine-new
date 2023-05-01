package searchengine.services.page;

import searchengine.model.domain.SiteDto;
import searchengine.model.entity.PageEntity;

import java.util.Optional;

public interface PageService {
    Optional<PageEntity> findPageEntityByPathAndSiteId(String path, int siteId);
    PageEntity save(PageEntity page);
    void deleteByPathAndSiteId(String path, int id);
    void createNewPage(String url, String content, int statusCode, SiteDto dto);
    int countBySiteId(int id);
}
