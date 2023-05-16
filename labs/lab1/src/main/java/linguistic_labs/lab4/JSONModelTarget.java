package linguistic_labs.lab4;

import java.util.HashMap;

public class JSONModelTarget extends JSONModelPart {
    public String lemma;
    public HashMap<String, String> disc_attributes;
    @Override
    public String toString() {
        return "{lemma: " + lemma + ", attributes: " + disc_attributes.toString() + "}";
    }
}
