package linguistic_labs.lab4;

import com.google.gson.*;
import linguistic_labs.commons.lemma.Lemma;
import linguistic_labs.commons.lemma.TextLemmatizer;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        TextLemmatizer textLemmatizer = new TextLemmatizer();
        List<Lemma> textLemmas = textLemmatizer.lemmatize(400, "bragunetzki/", ".txt");
        ModelFragmentFinder fragmentFinder = new ModelFragmentFinder(textLemmas);

        int models = 2;
        JSONModelParser parser = new JSONModelParser();
        for (int i = 1; i <= models; i++){
            JsonArray array = (JsonArray) JsonParser.parseReader(
                    new InputStreamReader(Objects.requireNonNull(Main.class.getClassLoader()
                            .getResourceAsStream("model_" + i + ".json"))));
            parser.setModelArray(array);
            ArrayList<JSONModelPart> model = parser.parseModel();
            List<List<Lemma>> fragments = fragmentFinder.findFragment(model, 1);

            System.out.print("Model: " + i + "; ");
            System.out.println("Occurences: " + fragments.size() + ";");
            for (List<Lemma> fragment : fragments) {
                printFragment(fragment);
            }
        }
    }

    public static void printFragment(List<Lemma> fragment) {
        for (Lemma lemma : fragment) {
            System.out.print(lemma.getLemmaForm() + " ");
        }
        System.out.println();
    }
}
