package linguistic_labs.lab4;

import com.google.gson.*;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        int models = 2;
        JSONModelParser parser = new JSONModelParser();
        for (int i = 1; i <= models; i++){
            JsonArray array = (JsonArray) JsonParser.parseReader(
                    new InputStreamReader(Objects.requireNonNull(Main.class.getClassLoader()
                            .getResourceAsStream("model_" + i + ".json"))));
            parser.setModelArray(array);
            ArrayList<JSONModelPart> model = parser.parseModel();
            System.out.println(model);
        }
    }
}
