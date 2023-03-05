import java.util.*;

public class Disambiguator {

    FrequencyDictionary dictionary;
    public Disambiguator(FrequencyDictionary dictionary) {
        this.dictionary = dictionary;
    }

    public Lemma pickOne(List<Lemma> lemmata) {
        if (lemmata.isEmpty()) {
            return null;
        }
        if (lemmata.size() == 1) {
            return lemmata.get(0);
        }
        double maxFreq = Double.MIN_VALUE;
        Lemma maxLemma = null;
        for (Lemma possibleLemma : lemmata) {
            Double currentMax = getMaxFrequencyForLemma(possibleLemma);
            if (currentMax != null && currentMax > maxFreq ) {
                maxFreq = currentMax;
                maxLemma = possibleLemma;
            }
        }
        return maxLemma;
    }

    private Double getMaxFrequencyForLemma(Lemma lemma) {
        Double currentMax = Double.MIN_VALUE;
        List<FrequencyDictionaryEntry> entries = dictionary.getEntriesForWord(lemma.getLemmaForm());
        if (entries == null) {
            return null;
        }
        for (FrequencyDictionaryEntry entry : dictionary.getEntriesForWord(lemma.getLemmaForm())) {
            if (entry.frequency() > currentMax
                    && OpencorporaToFreqrncPOSConverter.convert(lemma.getPartOfSpeech()).equals(entry.partOfSpeech())
            ) {
                currentMax = entry.frequency();
            }
        }
        return currentMax;
    }
}
