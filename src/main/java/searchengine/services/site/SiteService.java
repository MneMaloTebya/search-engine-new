package searchengine.services.site;


import searchengine.config.Site;
import searchengine.dto.StatusType;
import searchengine.model.entity.SiteEntity;

import java.util.Optional;

public interface SiteService {
    void deleteByUrl(String url);
    Optional<SiteEntity> findByUrl(String url);
    SiteEntity save(Site site, StatusType statusType);
    Optional<SiteEntity> findByUrlContains(String pageUrl);
    void setFailedStatus(Site site, String message);
    void setIndexedStatus(Site site);
}
