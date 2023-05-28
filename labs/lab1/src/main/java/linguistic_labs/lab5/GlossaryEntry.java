package linguistic_labs.lab5;

import java.util.Map;

public record GlossaryEntry(String word, Double weight, Map<String, Double> associated) {
}
