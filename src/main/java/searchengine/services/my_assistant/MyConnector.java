package searchengine.services.my_assistant;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import searchengine.model.domain.SiteDto;
import searchengine.services.indexing.IndexingServiceImpl;
import java.io.IOException;

public class MyConnector {

    private static final Log log = LogFactory.getLog(IndexingServiceImpl.class);

    public static Connection.Response getResponse(String linkPage, SiteDto dto) throws IOException {
        try {
            return Jsoup.connect(linkPage)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://google.com")
                    .timeout(0)
                    .execute();
        } catch (HttpStatusException e) {
            log.error("Ошибка подключения к странице: " + e.getUrl() + " Код ответа: ", e);
            throw new RuntimeException(e);
        }
    }
}
