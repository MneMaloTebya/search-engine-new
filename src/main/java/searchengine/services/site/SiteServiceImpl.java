package searchengine.services.site;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import searchengine.config.Site;
import searchengine.dto.StatusType;
import searchengine.model.PageRepository;
import searchengine.model.SiteRepository;
import searchengine.model.domain.SiteDto;
import searchengine.model.entity.SiteEntity;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SiteServiceImpl implements SiteService {

    private final SiteRepository siteRepository;
    private final PageRepository pageRepository;

    @Autowired
    public SiteServiceImpl(SiteRepository siteRepository, PageRepository pageRepository) {
        this.siteRepository = siteRepository;
        this.pageRepository = pageRepository;
    }

    @Override
    @Transactional
    public void deleteByUrl(String url) {
        Optional<SiteEntity> optionalSite = findByUrl(url);
        if (optionalSite.isPresent()) {
            pageRepository.deleteAllBySiteId(optionalSite.get().getId());
            siteRepository.deleteById(optionalSite.get().getId());
        }
    }

    @Override
    public Optional<SiteEntity> findByUrl(String url) {
        return siteRepository.findByUrl(url);
    }

    @Override
    public SiteEntity save(Site site, StatusType statusType) {
        SiteEntity siteEntity = new SiteEntity();
        siteEntity.setName(site.getName());
        siteEntity.setUrl(site.getUrl());
        siteEntity.setStatusTime(LocalDateTime.now());
        siteEntity.setStatusType(statusType);
        siteRepository.save(siteEntity);
        return siteEntity;
    }


    @Override
    public Optional<SiteEntity> findByUrlContains(String pageUrl) {
        return siteRepository.findByUrlContains(pageUrl);
    }

    @Override
    public void setFailedStatus(Site site, String message) {
        Optional<SiteEntity> optionalSite = siteRepository.findByUrl(site.getUrl());
        if (optionalSite.isPresent()) {
            SiteEntity entity = optionalSite.get();
            if (entity.getStatusType().equals(StatusType.INDEXING)) {
                entity.setStatusType(StatusType.FAILED);
                entity.setLastError(message);
                entity.setStatusTime(LocalDateTime.now());
                siteRepository.save(entity);
            }

        }
    }

    @Override
    public void setIndexedStatus(Site site) {
        Optional<SiteEntity> optionalSite = siteRepository.findByUrl(site.getUrl());
        if (optionalSite.isPresent()) {
            SiteEntity entity = optionalSite.get();
            entity.setStatusType(StatusType.INDEXED);
            entity.setStatusTime(LocalDateTime.now());
            siteRepository.save(entity);
        }
    }

}
