import java.util.ArrayList;

public class Trie<E> {
    private final TrieNode<E> root;

    public Trie() {
        this.root = new TrieNode<>(0);
    }

    public void add(String wordForm, E lemma) {
        root.add(wordForm, lemma);
    }

    public ArrayList<E> getResultsForWord(String wordForm) {
        return root.getLemmataForWord(wordForm);
    }
}
