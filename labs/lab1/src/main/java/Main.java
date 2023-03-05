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
        Disambiguator disambiguator = new Disambiguator(frequencyDictionary);

        HashMap<Lemma, StatisticContainer> resDictionary = new HashMap<>();
        for (int i = 0; i < textsInCorpora; i++) {
            String filename = prefix + i + postfix;
            ArrayList<String> words = WordParser.parseRussianWordsFromCorpora(
                    Objects.requireNonNull(Main.class.getClassLoader().getResourceAsStream(filename)));
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
                        StatisticContainer stats = resDictionary.getOrDefault(lemma, new StatisticContainer(textsInCorpora));
                        stats.incrementOccurrencesInText(i);
                        resDictionary.put(lemma, stats);
                    }
                    sum++;
                }
            }
        }

        for (StatisticContainer stat : resDictionary.values()) {
            stat.calculateTotalStats(sum);
        }

        ArrayList<Lemma> sortedLemmas = new ArrayList<>(resDictionary.keySet());
        sortedLemmas.sort(Comparator.comparing(x -> resDictionary.get(x).getFrequency()));
        int printed = 0;
        for (int i = 0; i < sortedLemmas.size() && printed < 100; i++) {
            Lemma currentLemma = sortedLemmas.get(sortedLemmas.size() - i - 1);
            if (!isRubbish(currentLemma)) {
                System.out.println(currentLemma + " --- " + resDictionary.get(currentLemma).toString());
                printed++;
            }
        }
    }
    private static boolean isRubbish(Lemma l) {
        if (l == null) {
            return false;
        }
        return switch (l.getPartOfSpeech()) {
            case "PREP" ,"CONJ","PRCL","INTJ" -> true;
            default -> false;
        };
    }
}
