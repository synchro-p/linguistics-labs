package linguistic_labs.commons.lemma;

import linguistic_labs.commons.freq.CsvFrequencyDictionaryReader;
import linguistic_labs.commons.freq.Disambiguator;
import linguistic_labs.commons.freq.FrequencyDictionary;
import linguistic_labs.lab1.Main;
import linguistic_labs.lab1.WordParser;

import javax.xml.stream.XMLStreamException;
import java.io.FileNotFoundException;
import java.util.*;

public class TextLemmatizer {

    public List<Lemma> lemmatize(int textCount, String prefix, String postfix) {
        Lemmatizer lemmatizer;
        try {
            lemmatizer = new Lemmatizer("dict.opcorpora.xml");
        } catch (FileNotFoundException | XMLStreamException e) {
            throw new RuntimeException("could not access specified dictionary");
        }

        FrequencyDictionary frequencyDictionary =
                new CsvFrequencyDictionaryReader(
                        Main.class.getClassLoader().getResourceAsStream("freqrnc2011.csv")).parseFrequencies();
        Disambiguator disambiguator = new Disambiguator(frequencyDictionary);

        List<Lemma> textLemmas = new ArrayList<>();
        for (int i = 0; i < textCount; i++) {
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
                    if (lemma != null)
                        textLemmas.add(lemma);
                }
            }
        }

        return textLemmas;
    }
}
