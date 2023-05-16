package linguistic_labs.lab4;

import java.util.ArrayList;
import java.util.HashMap;

public class JSONModelValency extends JSONModelPart {
    public ArrayList<HashMap<String, String>> variants;
    @Override
    public String toString() {
        return "{variants: " + variants.toString() + "}";
    }
}
