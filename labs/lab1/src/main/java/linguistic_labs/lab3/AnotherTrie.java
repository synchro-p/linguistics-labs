package linguistic_labs.lab3;

import java.util.List;
import java.util.Map;

public class AnotherTrie<E> {

    private final AnotherTrieNode<E> root;

    public AnotherTrie() {
        this.root = new AnotherTrieNode<>();
    }

    public void add(List<String> navigational, E entry) {
        root.add(navigational, entry);
    }

    public Map<List<String>, List<E>> traverse() {
        return root.traverse(List.of());
    }
}
