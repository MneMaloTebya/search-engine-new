package searchengine;

import org.apache.lucene.morphology.LuceneMorphology;
import org.apache.lucene.morphology.russian.RussianLuceneMorphology;
import searchengine.services.lemmatizer.LemmaFinder;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class LemmaFinderTest {
    public static void main(String[] args) throws IOException {
//        LuceneMorphology luceneMorph = new RussianLuceneMorphology();
//        List<String> wordBaseForms = luceneMorph.getNormalForms("леса");
//        wordBaseForms.forEach(System.out::println);


        Map<String, Integer> lemmas;
        String text = "Повторное появление леопарда в Осетии позволяет предположить,\n" +
                "что леопард постоянно обитает в некоторых районах Северного Кавказа.";
        LemmaFinder lemmaFinder = LemmaFinder.getInstance();
        lemmas = lemmaFinder.collectLemmas(text);
        System.out.println(lemmas.toString());
    }
}
