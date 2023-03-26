package linguistic_labs.lab2;

import linguistic_labs.commons.freq.CsvFrequencyDictionaryReader;
import linguistic_labs.commons.freq.Disambiguator;
import linguistic_labs.commons.freq.FrequencyDictionary;
import linguistic_labs.commons.lemma.Lemmatizer;

import javax.xml.stream.XMLStreamException;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConcordMain {
    public static void main(String[] args) {
        Lemmatizer lemmatizer;
        try {
            lemmatizer = new Lemmatizer("dict.opcorpora.xml");
        } catch (FileNotFoundException | XMLStreamException e) {
            throw new RuntimeException("could not access specified dictionary");
        }

        FrequencyDictionary frequencyDictionary =
                new CsvFrequencyDictionaryReader(
                        ConcordMain.class.getClassLoader().getResourceAsStream("freqrnc2011.csv")).parseFrequencies();
        Disambiguator disambiguator = new Disambiguator(frequencyDictionary);

        ConcordTokenizer tokenizer = new ConcordTokenizer();
        ConcordNormalizer normalizer = new ConcordNormalizer(lemmatizer, disambiguator);

        String a = "мама мыла раму собарнаяавы";

        tokenizer.tokenizeText(new ByteArrayInputStream(a.getBytes(StandardCharsets.UTF_8)));
        List<String> normalizedTokens = normalizer.normalizeWords(tokenizer.getWords());

        HashMap<Context, Integer> map = new HashMap<>();
        //get that from ContextFinder
        ConcordanceFinder finder = new ConcordanceFinder(normalizedTokens);

        List<Context> contexts = finder.getCorcordance(List.of("мама"), 1);
        for (Context c : contexts) {
            if (map.containsKey(c)) {
                map.put(c, map.get(c)+1);
            }
            else {
                map.put(c, 1);
            }
        }

        //todo sort contexts

        for (Map.Entry<Context, Integer> e : map.entrySet()) {
            System.out.println(e.getKey().getTokens() + " : " + e.getValue());
        }
    }
}
