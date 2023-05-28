package linguistic_labs.lab4;

import linguistic_labs.commons.lemma.Lemma;

import java.util.HashMap;
import java.util.List;

public class JSONModelValidator {
    public static boolean isPartValid(JSONModelPart part, Lemma lemma) {
        if (part instanceof JSONModelTarget target) {
            if (target.lemma.equals(lemma.getLemmaForm())) {
                for (String attr : target.disc_attributes.values()) {
                    if (!lemma.getGrammemes().contains(attr)) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        } else if (part instanceof JSONModelValency valency) {
            for (HashMap<String, String> variant : valency.variants) {

                boolean fits = true;
                for (String attr : variant.values()) {
                    if (!lemma.getGrammemes().contains(attr)) {
                        fits = false;
                        break;
                    }
                }
                if (fits) return true;
            }
        }

        return false;
    }

    public static boolean isSequenceValid(List<JSONModelPart> modelParts, List<Lemma> lemmas) {
        if (modelParts.size() > lemmas.size()) return false;

        for (int i = 0; i < modelParts.size(); i++) {
            if (!isPartValid(modelParts.get(i), lemmas.get(i)))
                return false;
        }
        return true;
    }
}
