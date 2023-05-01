package searchengine.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import searchengine.services.page_parser.PageValidator;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "indexing-settings")
public class SitesList {
    private List<Site> sites;

    public boolean urlIsLocatedConfig(String url) {
        List<String> siteUrl = new ArrayList<>();
        for (Site site : sites) {
            siteUrl.add(site.getUrl());
        }
        for (String site : siteUrl) {
            if (site.contains(PageValidator.getHostFromUrl(url))) {
                return true;
            }
        }
        return false;
    }
}
