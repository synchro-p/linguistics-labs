package linguistic_labs.commons.freq;

import linguistic_labs.commons.trie.Trie;

import java.util.List;

public class FrequencyDictionary {

    private final Trie<FrequencyDictionaryEntry> trie;

    public FrequencyDictionary() {
        this.trie = new Trie<>();
    }

    public void add(String word, FrequencyDictionaryEntry entry) {
        trie.add(word, entry);
    }

    public List<FrequencyDictionaryEntry> getEntriesForWord(String word) {
        return trie.getResultsForWord(word);
    }
}
