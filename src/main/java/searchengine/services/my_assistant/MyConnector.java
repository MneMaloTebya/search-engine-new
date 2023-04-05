package searchengine.services.my_assistant;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import searchengine.model.domain.SiteDto;
import searchengine.services.index_assistant.DataInserterService;
import searchengine.services.indexing.IndexServiceImpl;
import searchengine.services.page_parser.PageValidator;
import searchengine.services.site.SiteService;
import java.io.IOException;

@Service
public class MyConnector {

    private static final Log log = LogFactory.getLog(IndexServiceImpl.class);
    private final DataInserterService dataInserterService;

    @Autowired
    public MyConnector(DataInserterService dataInserterService) {
        this.dataInserterService = dataInserterService;
    }

    public Connection.Response getResponse(String linkPage, SiteDto dto) throws IOException {
        try {
            return Jsoup.connect(linkPage)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://google.com")
                    .timeout(0).execute();
        } catch (HttpStatusException e) {
            String path = PageValidator.getPathFromUrl(linkPage);
            dataInserterService.insertBadUrlToDB(dto, path, e.getStatusCode());
            log.error("Ошибка подключения к странице: " + e.getUrl() + " Код ответа: ", e);
            throw new RuntimeException(e);
        }
    }
}
