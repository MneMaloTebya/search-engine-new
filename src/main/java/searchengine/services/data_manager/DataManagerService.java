package searchengine.services.data_manager;

import searchengine.model.domain.PageDto;
import searchengine.model.domain.SiteDto;

public interface DataManagerService {
    void insertDataPageToDB(SiteDto dto, String pageUrl, int statusCode, String content, String text);
    void insertBadUrlToDB(SiteDto dto, String path, int statusCode);
    void insertLemmaAndIndexToDB(SiteDto siteDto, PageDto pageDto, String text);
    void deleteLemmaAndIndexByPagePath(SiteDto siteDto, PageDto pageDto);
    void insertNewPage(String url, SiteDto siteDto);
}
