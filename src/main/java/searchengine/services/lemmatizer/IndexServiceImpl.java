package searchengine.services.lemmatizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import searchengine.model.IndexRepository;
import searchengine.model.entity.IndexEntity;

import java.util.List;

@Service
public class IndexServiceImpl implements IndexService{

    private final IndexRepository indexRepository;

    @Autowired
    public IndexServiceImpl(IndexRepository indexRepository) {
        this.indexRepository = indexRepository;
    }

    @Override
    public IndexEntity save(IndexEntity entity) {
        return indexRepository.save(entity);
    }

    @Override
    @Transactional
    public void deleteAllByPageId(int id) {
        indexRepository.deleteAllByPageId(id);
    }

    @Override
    public List<IndexEntity> findAllByPageId(int id) {
        return indexRepository.findAllByPageId(id);
    }
}
