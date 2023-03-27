package linguistic_labs.lab2;

import linguistic_labs.commons.freq.Disambiguator;
import linguistic_labs.commons.lemma.Lemma;
import linguistic_labs.commons.lemma.Lemmatizer;

import java.util.ArrayList;
import java.util.List;

public class ConcordNormalizer {
    private final Lemmatizer lemmatizer;
    private final Disambiguator disambiguator;

    public ConcordNormalizer(Lemmatizer lemmatizer, Disambiguator disambiguator) {
        this.lemmatizer = lemmatizer;
        this.disambiguator = disambiguator;
    }

    public List<String> normalizeWords(List<String> rawWords) {
        ArrayList<String> res = new ArrayList<>();
        for (String word : rawWords) {
            List<Lemma> lemmata = lemmatizer.findLemmas(word);
            if (lemmata == null || lemmata.isEmpty()) {
                res.add(word);
            }
            else {
                res.add(disambiguator.pickOne(lemmata).getLemmaForm());
            }
        }
        return res;
    }
}
