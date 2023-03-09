import java.util.List;
import java.util.regex.Pattern;

public class Lemma {
    private final List<String> grammemes;
    private final Pattern capsPattern;

    public Lemma(String lemmaForm, List<String> grammemes) {
        this.lemmaForm = lemmaForm;
        this.grammemes = grammemes;
        this.capsPattern = Pattern.compile("\\p{Lu}*");
    }

    private final String lemmaForm;

    public String getLemmaForm() {
        return lemmaForm;
    }

    public List<String> getGrammemes() { return grammemes; }

    public String getPartOfSpeech() {
        for (String g : grammemes) {
            if (capsPattern.matcher(g).matches()) {
                return g;
            }
        }
        return "UNDEF";
    }

    public String toString() {
        StringBuilder builder = new StringBuilder(lemmaForm);
        for (String s : grammemes) {
            builder.append(", ").append(s);
        }
        builder.append(";");
        return builder.toString();
    }
}
