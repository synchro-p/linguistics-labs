package linguistic_labs.lab5;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class JSONDictParser {
    private JsonArray array;
    private final Gson gson = new Gson();

    public JSONDictParser() {

    }

    public void setModelArray(JsonArray array) {
        this.array = array;
    }

    public Map<String, GlossaryEntry> parse() {
        Map<String, GlossaryEntry> dict = new HashMap<>();
        for (JsonElement e : array) {
            JsonObject next = (JsonObject) e;
            String word = next.get("word").getAsString();
            GlossaryEntry entry = gson.fromJson(next, GlossaryEntry.class);
            dict.put(word, entry);
        }

        return dict;
    }
}
