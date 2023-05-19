package searchengine.services.lemmatizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import searchengine.model.LemmaRepository;
import searchengine.model.entity.LemmaEntity;

import java.util.List;
import java.util.Optional;

@Service
public class LemmaServiceImpl implements LemmaService {

    private final LemmaRepository lemmaRepository;

    @Autowired
    public LemmaServiceImpl(LemmaRepository lemmaRepository) {
        this.lemmaRepository = lemmaRepository;
    }


    @Override
    public List<LemmaEntity> findAllBySiteId(int id) {
        return lemmaRepository.findAllBySiteId(id);
    }

    @Override
    public Optional<LemmaEntity> findByLemma(String lemma) {
        return lemmaRepository.findByLemma(lemma);
    }

    @Override
    @Transactional
    public void deleteAllBySiteId(int id) {
        lemmaRepository.deleteAllBySiteId(id);
    }

    @Override
    public LemmaEntity save(LemmaEntity lemma) {
        return lemmaRepository.save(lemma);
    }

    @Override
    public int countBySiteId(int id) {
        return lemmaRepository.countBySiteId(id);
    }

    @Override
    public List<LemmaEntity> findAll() {
        return lemmaRepository.findAll();
    }

}
