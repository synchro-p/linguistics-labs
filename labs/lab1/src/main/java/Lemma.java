package main.java;

import java.util.ArrayList;
import java.util.List;

public class Lemma {

    public Lemma(String lemmaForm, List<String> wordForms) {
        this.lemmaForm = lemmaForm;
        this.wordForms = new ArrayList<>(wordForms);
    }

    private final String lemmaForm;
    private final List<String> wordForms;

    public List<String> getWordForms() {
        return wordForms;
    }

    public String getLemmaForm() {
        return lemmaForm;
    }
}
