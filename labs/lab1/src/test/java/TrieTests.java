import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TrieTests {
    @Test
    public void testAddAndSearch() {
        Trie trie = new Trie();
        Lemma lemma = new Lemma("побежать", List.of("побежало"));
        trie.add("побежал", lemma);
        assertEquals(lemma, trie.getLemmasForWord("побежал").get(0));
    }

    @Test
    public void multipleLemmasForOneForm() {
        Trie trie = new Trie();
        Lemma lemma = new Lemma("весить", List.of("вешу", "весит", "весь"));
        Lemma anotherLemma = new Lemma("весь", List.of("всего", "всему", "всем", "весь"));
        trie.add("весь", lemma);
        trie.add("весь", anotherLemma);
        List<Lemma> lemmata = trie.getLemmasForWord("весь");
        assertEquals(lemmata.get(0), lemma);
        assertEquals(lemmata.get(1), anotherLemma);
    }

    @Test
    public void testDisambiguation() {
        Trie trie = new Trie();
        Lemma lemma = new Lemma("весить", List.of("вешу", "весит", "весь"));
        Lemma anotherLemma = new Lemma("весь", List.of("всего", "всему", "всем", "весь"));
        trie.add("весь", lemma);
        trie.add("всего", anotherLemma);
        assertEquals(trie.getLemmasForWord("весь").get(0), lemma);
        assertEquals(trie.getLemmasForWord("всего").get(0), anotherLemma);
    }
}
