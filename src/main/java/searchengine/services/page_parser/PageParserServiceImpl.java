package searchengine.services.page_parser;

import org.jsoup.HttpStatusException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import searchengine.model.domain.SiteDto;
import searchengine.model.entity.SiteEntity;
import searchengine.services.index_assistant.DataInserterService;
import searchengine.services.my_assistant.MyConnector;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@Service
public class PageParserServiceImpl implements PageParserService {

    private final DataInserterService dataInserterService;
    private final MyConnector myConnector;

    @Autowired
    public PageParserServiceImpl(DataInserterService dataInserterService, MyConnector myConnector) {
        this.dataInserterService = dataInserterService;
        this.myConnector = myConnector;
    }

    @Override
    public Set<String> parsing(String pageUrl, SiteDto dto) {
        Set<String> urls = new HashSet<>();
        try {
            var response = myConnector.getResponse(pageUrl, dto);
            Document document = response.parse();
            int statusCode = response.statusCode();
            String content = document.outerHtml();
            dataInserterService.insertDataPageToDB(dto, pageUrl, statusCode, content);
            Elements elements = document.select("a");
            for (Element element : elements) {
                String url = element.attr("href");
                if (url.startsWith("/")) {
                    url = dto.getUrl() + url.substring(1);
                    if (PageValidator.urlNotContainsStopWords(url)) {
                        if (!PageValidator.getUrlsSite().contains(url)) {
                            urls.add(url);
                            PageValidator.getUrlsSite().add(url);
                        }
                    }
                }
                else if (url.startsWith("http") && url.contains(dto.getName().toLowerCase(Locale.ROOT))) {
                    if (PageValidator.urlNotContainsStopWords(url)) {
                        if (!PageValidator.getUrlsSite().contains(url)) {
                            urls.add(url);
                            PageValidator.getUrlsSite().add(url);
                        }
                    }
                }
            }
        } catch (HttpStatusException e) {
            return Collections.emptySet();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return urls;
    }
}
