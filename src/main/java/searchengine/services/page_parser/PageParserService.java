package searchengine.services.page_parser;

import searchengine.model.domain.SiteDto;
import java.util.Set;

public interface PageParserService {
    Set<String> parsing(String pageUrl, SiteDto siteDto);
}
