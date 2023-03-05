import java.util.List;

public class Lemma {
    List<String> grammemes;

    public Lemma(String lemmaForm, List<String> grammemes) {
        this.lemmaForm = lemmaForm;
        this.grammemes = grammemes;
    }

    private final String lemmaForm;

    public String getLemmaForm() {
        return lemmaForm;
    }

    public List<String> getGrammemes() { return grammemes; }
}
