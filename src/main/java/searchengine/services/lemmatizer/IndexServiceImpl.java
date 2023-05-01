package searchengine.services.lemmatizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import searchengine.model.IndexRepository;
import searchengine.model.entity.IndexEntity;

import java.util.Optional;

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
    public void deleteAllByPageId(int id) {
        indexRepository.deleteAllByPageId(id);
    }

    @Override
    public Optional<IndexEntity> findByPageId(int id) {
        return indexRepository.findByPageId(id);
    }
}
