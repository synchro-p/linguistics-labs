package linguistic_labs.lab3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnotherTrieNode<E> {
    private final HashMap<String, AnotherTrieNode<E>> transitions;
    private final ArrayList<E> entries;

    public AnotherTrieNode() {
        transitions = new HashMap<>();
        entries = new ArrayList<>();
    }

    public ArrayList<E> getEntries(List<String> navigational) {
        if (navigational.isEmpty()){
            return entries;
        }
        else {
            String toNavigate = navigational.get(0);
            if (!transitions.containsKey(toNavigate)) {
                return null;
            }
            return transitions.get(toNavigate).getEntries(navigational.subList(1, navigational.size()));
        }
    }

    public Map<List<String>, List<E>> traverse(List<String> list) {
        HashMap<List<String>, List<E>> res = new HashMap<>();
        if (!transitions.isEmpty()) {
            for (String s : transitions.keySet()) {
                List<String> newList = new ArrayList<>(list);
                newList.add(s);
                res.putAll(transitions.get(s).traverse(newList));
            }
        }
        else {
            res.put(list, entries);
        }
        return res;
    }

    public void add(List<String> navigational, E entry) {
        if (navigational.isEmpty()) {
            entries.add(entry);
        }
        else {
            String toNavigate = navigational.get(0);
            if (!transitions.containsKey(toNavigate)) {
                AnotherTrieNode<E> newNode = new AnotherTrieNode<>();
                transitions.put(toNavigate, newNode);
            }
            transitions.get(toNavigate).add(navigational.subList(1, navigational.size()), entry);
        }
    }
}
