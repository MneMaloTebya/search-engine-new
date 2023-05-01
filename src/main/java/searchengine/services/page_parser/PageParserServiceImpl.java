package searchengine.services.page_parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import searchengine.model.domain.PageDto;
import searchengine.model.domain.SiteDto;
import searchengine.services.index_assistant.DataManagerService;
import searchengine.services.my_assistant.MyConnector;

import java.io.IOException;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@Service
public class PageParserServiceImpl implements PageParserService {

    private final DataManagerService dataManagerService;

    @Autowired
    public PageParserServiceImpl(DataManagerService dataManagerService) {
        this.dataManagerService = dataManagerService;
    }

    @Override
    public Set<String> parsing(String pageUrl, SiteDto siteDto) {
        Set<String> urls = new HashSet<>();
        try {
            var response = MyConnector.getResponse(pageUrl, siteDto);
            Document document = response.parse();
            int statusCode = response.statusCode();
            if (statusCode > 400) {
                dataManagerService.insertBadUrlToDB(siteDto, pageUrl, statusCode);
            } else {
                String content = document.outerHtml();
                String text = document.text();
                dataManagerService.insertDataPageToDB(siteDto, pageUrl, statusCode, content, text);
                Elements elements = document.select("a");
                for (Element element : elements) {
                    String url = element.attr("href");
                    if (url.startsWith("/")) {
                        url = siteDto.getUrl() + url.substring(1);
                        if (PageValidator.urlNotContainsStopWords(url)) {
                            if (!PageValidator.getUrlsSite().contains(url)) {
                                urls.add(url);
                                PageValidator.getUrlsSite().add(url);
                            }
                        }
                    } else if (url.startsWith("http") && url.contains(siteDto.getName().toLowerCase(Locale.ROOT))) {
                        if (PageValidator.urlNotContainsStopWords(url)) {
                            if (!PageValidator.getUrlsSite().contains(url)) {
                                urls.add(url);
                                PageValidator.getUrlsSite().add(url);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return urls;
    }
}
