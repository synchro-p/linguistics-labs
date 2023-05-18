package linguistic_labs.lab4;

import linguistic_labs.commons.lemma.Lemma;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ModelFragmentFinder {
    private final List<Lemma> lemmaList;
    public ModelFragmentFinder(List<Lemma> lemmaList) {
        this.lemmaList = lemmaList;
    }

    public List<List<Lemma>> findFragment(List<JSONModelPart> model, int n) {
        List<List<Lemma>> result = new ArrayList<>();

        int windowSize = model.size() + n * 2;
        if (lemmaList.size() < windowSize) return null;


        List<Lemma> window = new LinkedList<>(lemmaList.subList(0, windowSize));

        for (int i = windowSize; i < lemmaList.size(); i++) {
            if (JSONModelValidator.isSequenceValid(model, window)) {
                result.add(new ArrayList<>(window));
            }

            Lemma lemma = lemmaList.get(i);
            window.remove(0);
            window.add(windowSize - 1, lemma);
        }
        if (JSONModelValidator.isSequenceValid(model, window)) {
            result.add(new ArrayList<>(window));
        }

        return result;
    }
}
