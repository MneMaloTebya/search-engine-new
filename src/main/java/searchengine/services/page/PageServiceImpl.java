package searchengine.services.page;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import searchengine.model.PageRepository;
import searchengine.model.domain.SiteDto;
import searchengine.model.entity.PageEntity;

import java.util.List;
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

    @Override
    @Transactional
    public void deleteByPathAndSiteId(String path, int id) {
        pageRepository.deleteByPathAndSiteId(path, id);
    }

    @Override
    public void createNewPage(String url, String content, int statusCode, SiteDto dto) {
        PageEntity pageEntity = new PageEntity();
        pageEntity.setPath(url);
        pageEntity.setCode(statusCode);
        pageEntity.setContent(content);
        pageEntity.setSiteId(dto.getId());
        pageRepository.save(pageEntity);
    }

    @Override
    public int countBySiteId(int id) {
        return pageRepository.countBySiteId(id);
    }

    @Override
    public List<PageEntity> findAllBySiteId(int id) {
        return pageRepository.findAllBySiteId(id);
    }

    @Override
    public Optional<PageEntity> findById(int id) {
        return pageRepository.findById(id);
    }
}
