package linguistic_labs.lab5;

import java.util.Map;

public record GlossaryEntry(String word, Map<String, Double> associated, Double wordWeight) {
}
