package linguistic_labs.lab5;

import linguistic_labs.commons.freq.CsvFrequencyDictionaryReader;
import linguistic_labs.commons.freq.Disambiguator;
import linguistic_labs.commons.freq.FrequencyDictionary;
import linguistic_labs.commons.lemma.Lemmatizer;
import linguistic_labs.lab2.ConcordNormalizer;
import linguistic_labs.lab2.ConcordTokenizer;

import javax.xml.stream.XMLStreamException;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
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

        Map<String, GlossaryEntry> glossary = new HashMap<>();
        //TODO glossary
        glossary.put("стекло", new GlossaryEntry("стекло", Map.of(), 1.0));
        glossary.put("ломать", new GlossaryEntry("ломать", Map.of("перелом", 0.6, "стекло", 0.3), 1.0));

        StringToQueryAdapter adapter = new StringToQueryAdapter(normalizer);

        Set<String> importantWords = new HashSet<>();
        for (String importantWord : glossary.keySet()) {
            importantWords.add(importantWord);
            importantWords.addAll(glossary.get(importantWord).associated().keySet());
        }
        HashMap<String, ArrayList<ArrayList<Integer>>> importantWordEntries = new HashMap<>();

        for (String s : importantWords) {
            ArrayList<ArrayList<Integer>> toPut = new ArrayList<>();
            for (int i = 0; i < 400; i++) {
                toPut.add(new ArrayList<>());
            }
            importantWordEntries.put(s, toPut);
        }

        ArrayList<Integer> lengths = new ArrayList<>(400);
        for (int i = 0; i < 400; i++) {
            ConcordTokenizer tokenizer = new ConcordTokenizer();
            tokenizer.tokenizeText(Main.class.getClassLoader().getResourceAsStream("corpora/samoseyko/" + i + ".txt"));
            List<String> normalizedWordsFromCorpora = normalizer.normalizeWords(tokenizer.getWords());

            int length = 0;
            for (String word : normalizedWordsFromCorpora) {
                if (importantWords.contains(word)) {
                    ArrayList<Integer> positionsForCurrentText = importantWordEntries.get(word).get(i);
                    positionsForCurrentText.add(length);
                }
                length++;
            }
            lengths.add(i, length);
        }

        BM25Evaluator evaluator = new BM25Evaluator(importantWordEntries, lengths);
        evaluator.calculateIDF();
        evaluator.calculateTF();
        evaluator.countAverageLength();

        //TODO adequate list
        ArrayList<String> requests = new ArrayList<>(List.of("стекла","стеклу", "ломал стекло", "ломаю"));

        ArrayList<Map<String, Double>> queries = new ArrayList<>();
        for (String request : requests) {
            queries.add(adapter.glossaryAdapt(request, glossary));
        }

        for (Map<String, Double> query : queries){
            QueryEvaluator queryEvaluator = new QueryEvaluator(evaluator);
            System.out.println("QUERY: " + query.toString());
            Map<Integer, Double> resultsByText = new HashMap<>();
            for (int i = 0; i < 400; i++) {
                resultsByText.put(i, queryEvaluator.evaluateQuery(i, query));
            }
            ArrayList<Map.Entry<Integer, Double>> sortedResults = new ArrayList<>(resultsByText.entrySet());
            sortedResults.sort(Comparator.comparing(e -> e.getValue() * -1));
            for (int i = 0; i < 10; i++) {
                Map.Entry<Integer, Double> toPrint = sortedResults.get(i);
                ArrayList<Integer> positionsOfQueryWords = new ArrayList<>();
                for (Map.Entry<String, Double> e : query.entrySet()) {
                    positionsOfQueryWords.addAll(importantWordEntries.get(e.getKey()).get(toPrint.getKey()));
                }
                positionsOfQueryWords.sort(Comparator.comparingInt(e -> e));
                System.out.println("Text #" + toPrint.getKey() + " with score " + toPrint.getValue() + " (positions " + positionsOfQueryWords +")");
            }
            System.out.println("********************");
        }
    }
}
