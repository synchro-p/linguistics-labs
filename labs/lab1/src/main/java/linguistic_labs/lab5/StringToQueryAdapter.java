package linguistic_labs.lab5;

import linguistic_labs.lab2.ConcordNormalizer;
import linguistic_labs.lab2.ConcordTokenizer;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StringToQueryAdapter {
    private final ConcordNormalizer normalizer;

    public StringToQueryAdapter(ConcordNormalizer normalizer){
        this.normalizer = normalizer;
    }

    public Map<String, Double> simpleAdapt(String query) {
        ConcordTokenizer tokenizer = new ConcordTokenizer();
        tokenizer.tokenizeText(new ByteArrayInputStream(query.getBytes()));
        List<String> normalizedWords = normalizer.normalizeWords(tokenizer.getWords());

        HashMap<String, Double> res = new HashMap<>();
        for(String word : normalizedWords) {
            res.put(word, 1.0);
        }
        return res;
    }

    public Map<String, Double> glossaryAdapt(String query, Map<String, GlossaryEntry> glossary) {
        ConcordTokenizer tokenizer = new ConcordTokenizer();
        tokenizer.tokenizeText(new ByteArrayInputStream(query.getBytes()));
        List<String> normalizedWords = normalizer.normalizeWords(tokenizer.getWords());

        HashMap<String, Double> res = new HashMap<>();
        for(String word : normalizedWords) {
            GlossaryEntry entry = glossary.get(word);
            if (entry != null) {
                res.put(entry.word(), entry.weight());
                res.putAll(entry.associated());
            }
        }
        return res;
    }
}
