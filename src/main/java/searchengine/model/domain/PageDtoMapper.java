package searchengine.model.domain;

import searchengine.model.entity.PageEntity;

public class PageDtoMapper {

    public static PageDto toDomain(PageEntity pageEntity) {
        PageDto pageDto = new PageDto();
        pageDto.setId(pageEntity.getId());
        pageDto.setPath(pageEntity.getPath());
        pageDto.setCode(pageEntity.getCode());
        pageDto.setContent(pageEntity.getContent());
        pageDto.setSiteId(pageEntity.getSiteId());
        return pageDto;
    }
}
