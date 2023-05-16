package linguistic_labs.lab4;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

public class JSONModelParser {
    private JsonArray modelArray;
    private final Gson gson = new Gson();
    public JSONModelParser(JsonArray modelArray) {
        this.modelArray = modelArray;
    }

    public JSONModelParser() {
    }

    public void setModelArray(JsonArray modelArray) {
        this.modelArray = modelArray;
    }

    public ArrayList<JSONModelPart> parseModel() {
        ArrayList<JSONModelPart> jsonModel  = new ArrayList<>();
        for (JsonElement a : modelArray) {
            JsonObject nextElement = (JsonObject) a;
            String type = nextElement.get("type").getAsString();
            nextElement.remove("type");
            if (type.equals("valency")) {
                jsonModel.add(gson.fromJson(nextElement, JSONModelValency.class));
            }
            else if (type.equals("target")) {
                jsonModel.add(gson.fromJson(nextElement, JSONModelTarget.class));
            }
        }
        return jsonModel;
    }
}
