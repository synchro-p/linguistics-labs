package linguistic_labs.lab5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.OptionalDouble;

public class BM25Evaluator {
    private final double k = 2;
    private final double b = 0.75;
    private double avglength;
    private final Map<String, ArrayList<ArrayList<Integer>>> entries;
    private final int texts;
    private final ArrayList<Integer> lengths;
    private final Map<String, ArrayList<Double>> tfs;
    private final Map<String, Double> idfs;

    public BM25Evaluator(Map<String, ArrayList<ArrayList<Integer>>> entries, ArrayList<Integer> lengths) {
        this.entries = entries;
        this.lengths = lengths;
        this.texts = lengths.size();
        this.idfs = new HashMap<>();
        this.tfs = new HashMap<>();
    }

    public void calculateTF() {
        for (String word : entries.keySet()) {
            for (int i = 0; i < texts; i++) {
                int entriesNumber = entries.get(word).get(i).size();
                ArrayList<Double> toPut = tfs.get(word);
                if(toPut == null) {
                    toPut = new ArrayList<>();
                }
                toPut.add(i, (double)entriesNumber / lengths.get(i));
                tfs.put(word, toPut);
            }
        }
    }

    public void calculateIDF() {
        for (String word : entries.keySet()) {
            int n = 0;
            for (int i = 0; i < texts; i++) {
                if (entries.get(word).get(i).size() > 0) {
                    n++;
                }
            }
            idfs.put(word, Math.log(lengths.size() / (double) n));
        }
    }

    public void countAverageLength() {
         OptionalDouble thingey = lengths
                .stream()
                .mapToDouble(a -> a)
                .average();
         avglength = thingey.isPresent() ? thingey.getAsDouble():0;
    }

    public double calculateMetric(int documentNumber, String words) {
        return idfs.get(words) * tfs.get(words).get(documentNumber) * (k + 1) /
                (tfs.get(words).get(documentNumber) + k * (1 - b + b * lengths.get(documentNumber) / avglength));
    }
}
