package searchengine.services.index_assistant;

import searchengine.model.domain.SiteDto;

public interface DataInserterService {
    void insertDataPageToDB(SiteDto dto, String pageUrl, int statusCode, String content);
    void insertBadUrlToDB(SiteDto dto, String path, int statusCode);

}
