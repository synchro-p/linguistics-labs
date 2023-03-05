import javax.xml.stream.XMLStreamException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {
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
                ArrayList<Lemma> lemmaList = lemmatizer.findLemmas(s);
                if (lemmaList != null) {
                    for (Lemma lemma : lemmaList) {
                        StatisticContainer stats = freqDictionary.getOrDefault(lemma, new StatisticContainer(textsInCorpora));
                        stats.incrementOccurrencesInText(i);
                        freqDictionary.put(lemma, stats);
                        System.out.print(s + " --> " + lemma.getLemmaForm());
                        for (String g : lemma.getGrammemes()) {
                            System.out.print(", " + g);
                        }
                        System.out.println(".");
                    }
                }
                sum++;
            }
        }

        System.out.println("**********");

        for (StatisticContainer stat : freqDictionary.values()) {
            stat.calculateTotalStats(sum);
        }

        for (Lemma lemma : freqDictionary.keySet()) {
            System.out.println(lemma.getLemmaForm() + ": " + freqDictionary.get(lemma).toString());
        }
    }
}