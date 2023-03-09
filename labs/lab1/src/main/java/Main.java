import javax.xml.stream.XMLStreamException;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        startup(400, "corpora/samoseyko/", ".txt");
    }

    private static void startup(int textsInCorpora, String prefix, String postfix) {
        int sum = 0;
        Lemmatizer lemmatizer;
        try {
            lemmatizer = new Lemmatizer("dict.opcorpora.xml");
        } catch (FileNotFoundException | XMLStreamException e) {
            throw new RuntimeException("could not access specified dictionary");
        }

        FrequencyDictionary frequencyDictionary = new CsvFrequencyDictionaryReader(Main.class.getResourceAsStream("freqrnc2011.csv")).parseFrequencies();
        System.out.println(frequencyDictionary.getEntriesForWord("формочка"));
        Disambiguator disambiguator = new Disambiguator(frequencyDictionary);

        HashMap<Lemma, StatisticContainer> freqDictionary = new HashMap<>();
        for (int i = 0; i < textsInCorpora; i++) {
            String filenameBuilder = prefix + i + postfix;
            ArrayList<String> words = WordParser.parseRussianWordsFromCorpora(
                    Objects.requireNonNull(Main.class.getClassLoader().getResourceAsStream(filenameBuilder)));
            for (String s : words) {
                ArrayList<Lemma> lemmaList = lemmatizer.findLemmas(s);
                List<String> splitStrings = new ArrayList<>();
                if (lemmaList == null && s.contains("-"))
                    splitStrings = Arrays.asList(s.split("-"));
                else splitStrings.add(s);

                for (String part : splitStrings) {
                    if (splitStrings.size() > 1) {
                        lemmaList = lemmatizer.findLemmas(part);
                    }
                    if (lemmaList != null) {
                        Lemma lemma = disambiguator.pickOne(lemmaList);
                        System.out.println(part + " --> " + (lemma == null ? "null" : lemma.toString()));
                        StatisticContainer stats = freqDictionary.getOrDefault(lemma, new StatisticContainer(textsInCorpora));
                        stats.incrementOccurrencesInText(i);
                        freqDictionary.put(lemma, stats);
                    }
                    sum++;
                }
            }
        }

        System.out.println("**********");

        for (StatisticContainer stat : freqDictionary.values()) {
            stat.calculateTotalStats(sum);
        }

        ArrayList<Lemma> sortedLemmas = new ArrayList<>(freqDictionary.keySet());
        sortedLemmas.sort(Comparator.comparingDouble(x -> freqDictionary.get(x).getFrequency()));
        for (Lemma lemma : sortedLemmas) {
            System.out.println((lemma == null ? "null":lemma.getLemmaForm()) + ": " + freqDictionary.get(lemma).toString());
        }
    }
}
