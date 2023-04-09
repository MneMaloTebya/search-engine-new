package searchengine.services.site;


import searchengine.config.Site;
import searchengine.dto.StatusType;
import searchengine.model.domain.SiteDto;
import searchengine.model.entity.SiteEntity;

import java.util.Optional;

public interface SiteService {
    void deleteByUrl(String url);
    Optional<SiteEntity> findByUrl(String url);
    SiteEntity save(Site site, StatusType statusType);
    SiteEntity changeStatus(SiteDto dto, StatusType statusType);
    Optional<SiteEntity> findByUrlContains(String pageUrl);
}
