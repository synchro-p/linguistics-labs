import java.util.ArrayList;
import java.util.HashMap;

public class TrieNode {
    private final Integer level;
    private final HashMap<Character, TrieNode> transitions;
    private final ArrayList<Lemma> lemmata;


    public void add(String wordForm, Lemma lemma) {
        if (wordForm.length() == level) {
            lemmata.add(lemma);
        }
        else if (transitions.containsKey(wordForm.charAt(level))) {
            transitions.get(wordForm.charAt(level)).add(wordForm, lemma);
        }
        else {
            TrieNode nextNode = new TrieNode(this.level+1);
            nextNode.add(wordForm, lemma);
            transitions.put(wordForm.charAt(level), nextNode);
        }
    }

    public TrieNode(Integer level) {
        this.level = level;
        transitions = new HashMap<>();
        lemmata = new ArrayList<>();
    }

    public ArrayList<Lemma> getLemmataForWord(String wordForm) {
        if (wordForm.length() == level) {
            return lemmata;
        }
        else if (transitions.containsKey(wordForm.charAt(level))){
            return transitions.get(wordForm.charAt(level)).getLemmataForWord(wordForm);
        }
        else return null;
    }
}
