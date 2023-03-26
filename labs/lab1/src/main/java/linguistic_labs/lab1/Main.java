package linguistic_labs.lab1;

import linguistic_labs.commons.freq.CsvFrequencyDictionaryReader;
import linguistic_labs.commons.freq.Disambiguator;
import linguistic_labs.commons.freq.FrequencyDictionary;
import linguistic_labs.commons.lemma.Lemma;
import linguistic_labs.commons.lemma.Lemmatizer;

import javax.xml.stream.XMLStreamException;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        startup(400, "corpora/samoseyko/", ".txt", false);
    }

    private static void startup(int textsInCorpora, String prefix, String postfix, boolean clearRubbishFlag) {
        int sum = 0;
        Lemmatizer lemmatizer;
        try {
            lemmatizer = new Lemmatizer("dict.opcorpora.xml");
        } catch (FileNotFoundException | XMLStreamException e) {
            throw new RuntimeException("could not access specified dictionary");
        }

        FrequencyDictionary frequencyDictionary =
                new CsvFrequencyDictionaryReader(
                        Main.class.getResourceAsStream("freqrnc2011.csv")).parseFrequencies();
        Disambiguator disambiguator = new Disambiguator(frequencyDictionary);

        HashMap<Lemma, StatisticContainer> resDictionary = new HashMap<>();
        for (int i = 0; i < textsInCorpora; i++) {
            String filename = prefix + i + postfix;
            ArrayList<String> words = WordParser.parseRussianWordsFromCorpora(
                    Objects.requireNonNull(Main.class.getClassLoader().getResourceAsStream(filename)));
            for (ListIterator<String> iterator = words.listIterator(); iterator.hasNext();) {
                String s = iterator.next();
                ArrayList<Lemma> lemmaList = lemmatizer.findLemmas(s);
                if (lemmaList == null && s.contains("-")) {
                    for (String part : s.split("-"))
                        iterator.add(part);
                    continue;
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

        for (StatisticContainer stat : resDictionary.values()) {
            stat.calculateTotalStats(sum);
        }

        ArrayList<Lemma> sortedLemmas = new ArrayList<>(resDictionary.keySet());
        sortedLemmas.sort(Comparator.comparing(x -> resDictionary.get(x).getFrequency()));

        int printed = 0;
        for (int i = 0; i < sortedLemmas.size() && printed < 100; i++) {
            Lemma currentLemma = sortedLemmas.get(sortedLemmas.size() - i - 1);
            if (currentLemma == null ||
                    (!currentLemma.getPartOfSpeech().isRubbish() && clearRubbishFlag) ||
                    (!clearRubbishFlag)) {
                System.out.println(currentLemma + " --- " + resDictionary.get(currentLemma).toString());
                printed++;
            }
        }
    }
}
