package searchengine.model.domain;

import searchengine.model.entity.SiteEntity;

public class SiteDtoMapper {

    public static SiteDto toDomain(SiteEntity siteEntity) {
        SiteDto dto = new SiteDto();
        dto.setId(siteEntity.getId());
        dto.setName(siteEntity.getName());
        dto.setUrl(siteEntity.getUrl());
        dto.setLastError(siteEntity.getLastError());
        dto.setStatusTime(siteEntity.getStatusTime());
        return dto;
    }
}
