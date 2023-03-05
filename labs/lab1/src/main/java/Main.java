import javax.xml.stream.XMLStreamException;
import java.io.FileNotFoundException;
import java.util.*;

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
                List<String> splitStrings = new ArrayList<>();
                if (lemmaList == null && s.contains("-"))
                    splitStrings = Arrays.asList(s.split("-"));
                else splitStrings.add(s);

                for (String part : splitStrings) {
                    if (splitStrings.size() > 1) {
                        lemmaList = lemmatizer.findLemmas(part);
                    }
                    if (lemmaList != null) {
                        for (Lemma lemma : lemmaList) {
                            StatisticContainer stats = freqDictionary.getOrDefault(lemma, new StatisticContainer(textsInCorpora));
                            stats.incrementOccurrencesInText(i);
                            freqDictionary.put(lemma, stats);
                        }
                    }
                    printLemmas(part, lemmaList);
                    sum++;
                }
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

    public static void printLemmas(String form, ArrayList<Lemma> lemmas) {
        System.out.print(form + " --> ");
        if (lemmas != null) {
            for (Lemma lemma : lemmas) {
                System.out.print(lemma.getLemmaForm());
                for (String g : lemma.getGrammemes()) {
                    System.out.print(", " + g);
                }
                System.out.print("; ");
            }
        }
        System.out.println();
    }
}
