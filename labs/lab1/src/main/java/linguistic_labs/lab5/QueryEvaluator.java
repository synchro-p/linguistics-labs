package linguistic_labs.lab5;

import java.util.Collection;
import java.util.Map;

public class QueryEvaluator {
    private final BM25Evaluator evaluator;

    public QueryEvaluator(BM25Evaluator evaluator) {
        this.evaluator = evaluator;
    }

    public Double evaluateQuery(int documentNumber, Collection<String> words) {
        double sum = 0.0;
        for (String s : words) {
            sum += evaluator.calculateMetric(documentNumber, s);
        }
        return sum;
    }

    public Double evaluateQuery(int documentNumber, Map<String, Double> words) {
        double sum = 0.0;
        for (String s : words.keySet()) {
            sum += evaluator.calculateMetric(documentNumber, s) * words.get(s);
        }
        return sum;
    }
}
