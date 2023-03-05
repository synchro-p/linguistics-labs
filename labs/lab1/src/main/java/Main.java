import javax.xml.stream.XMLStreamException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {
//        String s = "какой-то";
//        try {
//            Lemmatizer lemmatizer = new Lemmatizer("dict.opcorpora.xml");
//            System.out.println(lemmatizer.findLemmaForm(s));
//        } catch (XMLStreamException | FileNotFoundException e) {
//            e.printStackTrace();
//        }
        startup(2, "corpora/samoseyko/", ".txt");
    }

    private static void startup(int textsInCorpora, String prefix, String postfix) {
        int sum = 0;
        Lemmatizer lemmatizer;
        try {
            lemmatizer = new Lemmatizer("dict.opcorpora.xml");
        } catch (FileNotFoundException | XMLStreamException e) {
            throw new RuntimeException("could not access specified dictionary");
        }
        HashMap<Lemma, StatisticContainer> freqDictionary = new HashMap<>();
        for (int i = 0; i < textsInCorpora; i++) {
            String filenameBuilder = prefix + i + postfix;
            ArrayList<String> words = WordParser.parseRussianWordsFromCorpora(
                Objects.requireNonNull(Main.class.getClassLoader().getResourceAsStream(filenameBuilder)));
            for (String s : words) {
                Lemma lemma = lemmatizer.findLemma(s);
                StatisticContainer stats = freqDictionary.getOrDefault(lemma, new StatisticContainer(textsInCorpora));
                stats.incrementOccurrencesInText(i);
                freqDictionary.put(lemma, stats);
                sum++;
            }
        }

        for (StatisticContainer stat : freqDictionary.values()) {
            stat.calculateTotalStats(sum);
        }

        for (Lemma s : freqDictionary.keySet()) {
            System.out.println(s.getLemmaForm() + ": " + freqDictionary.get(s).toString());
        }
    }
}