package searchengine.services.index_assistant;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import searchengine.model.domain.SiteDto;
import searchengine.model.entity.PageEntity;
import searchengine.model.entity.SiteEntity;
import searchengine.services.indexing.IndexServiceImpl;
import searchengine.services.page.PageService;
import searchengine.services.page_parser.PageValidator;

import java.util.Optional;

@Service
public class DataInserterServiceImpl implements DataInserterService{

    private static final Log log = LogFactory.getLog(IndexServiceImpl.class);
    private final PageService pageService;

    public DataInserterServiceImpl(PageService pageService) {
        this.pageService = pageService;
    }

    @Override
    public void insertDataPageToDB(SiteDto dto, String pageUrl, int statusCode, String content) {
        String path = PageValidator.getPathFromUrl(pageUrl);
        Optional<PageEntity> optionalPage = pageService.findPageEntityByPathAndSiteId(path, dto.getId());
        if (optionalPage.isEmpty()) {
            PageEntity page = new PageEntity();
            page.setPath(path);
            page.setSiteId(dto.getId());
            page.setContent(content);
            page.setCode(statusCode);
            pageService.save(page);
            log.info("Добавили путь: " + path + " Сайта: " + dto.getName());
        }
    }

    @Override
    public void insertBadUrlToDB(SiteDto dto, String path, int statusCode) {
        Optional<PageEntity> optionalPage = pageService.findPageEntityByPathAndSiteId(path, dto.getId());
        if (optionalPage.isEmpty()) {
            PageEntity page = new PageEntity();
            page.setPath(path);
            page.setSiteId(dto.getId());
            page.setCode(statusCode);
            page.setContent("-");
            pageService.save(page);
            log.info("Добавили путь: " + path + " Сайта: " + dto.getName() + statusCode);
        }
    }
}
