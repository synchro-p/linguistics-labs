package linguistic_labs.lab2;

import linguistic_labs.commons.freq.CsvFrequencyDictionaryReader;
import linguistic_labs.commons.freq.Disambiguator;
import linguistic_labs.commons.freq.FrequencyDictionary;
import linguistic_labs.commons.lemma.Lemmatizer;

import javax.xml.stream.XMLStreamException;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.*;

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

        String a = "мама мыла раму абырвалг мама мыла мама раму мыла мылом мыть умыть";

        tokenizer.tokenizeText(new ByteArrayInputStream(a.getBytes(StandardCharsets.UTF_8)));
        List<String> normalizedTokens = normalizer.normalizeWords(tokenizer.getWords());

        HashMap<Context, Integer> map = new HashMap<>();
        ConcordanceFinder finder = new ConcordanceFinder(normalizedTokens);

        List<Context> contexts = finder.getCorcordance(List.of("мама"), 2);
        for (Context c : contexts) {
            map.merge(c, 1, Integer::sum);
        }

        Comparator<Context> sortComparator = Comparator.<Context>comparingInt((c -> c.getContextType().getPriority())).
                thenComparingInt(c -> map.get(c) * -1).thenComparingInt(c -> c.getLength() * -1);

        ArrayList<Context> sortedContexts = new ArrayList<>(map.keySet().stream().toList());
        sortedContexts.sort(sortComparator);

        for (Context c : sortedContexts) {
            System.out.println(c.getTokens() + " : " + map.get(c));
        }
    }
}
