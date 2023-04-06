package linguistic_labs.lab3;

import linguistic_labs.commons.freq.CsvFrequencyDictionaryReader;
import linguistic_labs.commons.freq.Disambiguator;
import linguistic_labs.commons.freq.FrequencyDictionary;
import linguistic_labs.commons.lemma.Lemmatizer;
import linguistic_labs.lab2.ConcordNormalizer;
import linguistic_labs.lab2.ConcordTokenizer;

import javax.xml.stream.XMLStreamException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        int textsInCorpora = 400;
        int N = 3;
        String prefix = "corpora/samoseyko/";
        String postfix = ".txt";

        Lemmatizer lemmatizer;
        try {
            lemmatizer = new Lemmatizer("dict.opcorpora.xml");
        } catch (FileNotFoundException | XMLStreamException e) {
            throw new RuntimeException("could not access specified dictionary");
        }

        FrequencyDictionary frequencyDictionary =
                new CsvFrequencyDictionaryReader(
                        linguistic_labs.lab1.Main.class.getClassLoader().getResourceAsStream("freqrnc2011.csv")).parseFrequencies();
        Disambiguator disambiguator = new Disambiguator(frequencyDictionary);

        ConcordNormalizer normalizer = new ConcordNormalizer(lemmatizer, disambiguator);

        int sum = 0;

        List<List<String>> texts = new ArrayList<>();
        AnotherTrie<int[]> trie = new AnotherTrie<>();
        for (int i = 0; i < textsInCorpora; i++) {
            String filename = prefix + i + postfix;
            ConcordTokenizer tokenizer = new ConcordTokenizer();
            tokenizer.tokenizeText(Main.class.getClassLoader().getResourceAsStream(filename));
            List<String> normalizedWordsFromCorpora = normalizer.normalizeWords(tokenizer.getWords());
            texts.add(normalizedWordsFromCorpora);
            for (int j = 0; j < normalizedWordsFromCorpora.size()-N; j++) {
                sum++;
                int[] position = {i,j};
                trie.add(normalizedWordsFromCorpora.subList(j, j+N), position);
            }
        }
        Map<List<String>, List<int[]>> res = trie.traverse();
        for (List<String> ngram : res.keySet()) {
            List<int[]> positions = res.get(ngram);
            if(positions.size()>3){
                System.out.print(ngram + ": ");
                for (int[] i : positions) {
                    System.out.print("T " + i[0] + ", P " + i[1] + "; ");
                }
                System.out.println();
            }
        }
    }
}
