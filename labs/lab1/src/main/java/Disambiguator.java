import java.util.List;

public class Disambiguator {

    public Disambiguator() {

    }

    public Lemma pickOne(List<Lemma> lemmata) {
        return (lemmata.isEmpty() ? null : lemmata.get(0));
    }
}
