package searchengine.services.lemmatizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import searchengine.model.LemmaRepository;
import searchengine.model.entity.LemmaEntity;

import java.util.Optional;

@Service
public class LemmaServiceImpl implements LemmaService {

    private final LemmaRepository lemmaRepository;

    @Autowired
    public LemmaServiceImpl(LemmaRepository lemmaRepository) {
        this.lemmaRepository = lemmaRepository;
    }


    @Override
    public Optional<LemmaEntity> findBySiteId(int id) {
        return lemmaRepository.findBySiteId(id);
    }

    @Override
    public Optional<LemmaEntity> findByLemma(String lemma) {
        return lemmaRepository.findByLemma(lemma);
    }

    @Override
    public void deleteAllBySiteId(int id) {
        lemmaRepository.deleteAllBySiteId(id);
    }

    @Override
    public LemmaEntity save(LemmaEntity lemma) {
        return lemmaRepository.save(lemma);
    }
}
