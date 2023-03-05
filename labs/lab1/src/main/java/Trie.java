import java.util.ArrayList;

public class Trie {
    private final TrieNode root;

    public Trie() {
        this.root = new TrieNode(0);
    }

    public void add(String wordForm, Lemma lemma) {
        root.add(wordForm, lemma);
    }

    public ArrayList<Lemma> getLemmasForWord(String wordForm) {
        return root.getLemmataForWord(wordForm);
    }
}
