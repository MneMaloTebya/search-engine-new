package searchengine.services.page;

import org.springframework.stereotype.Service;
import searchengine.model.PageRepository;
import searchengine.model.entity.PageEntity;

import java.util.Optional;

@Service
public class PageServiceImpl implements PageService{

    private final PageRepository pageRepository;

    public PageServiceImpl(PageRepository pageRepository) {
        this.pageRepository = pageRepository;
    }

    @Override
    public Optional<PageEntity> findPageEntityByPathAndSiteId(String path, int siteId) {
        return pageRepository.findPageEntityByPathAndSiteId(path, siteId);
    }

    @Override
    public PageEntity save(PageEntity page) {
        return pageRepository.save(page);
    }
}
