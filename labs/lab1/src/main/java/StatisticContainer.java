import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

public class StatisticContainer {
    private final ArrayList<Integer> occurrencesByText;
    private Double frequency;
    private Double textFrequency;
    private final Integer texts;


    public StatisticContainer(int texts) {
        this.occurrencesByText = new ArrayList<>(Collections.nCopies(texts, 0));
        this.texts = texts;
        frequency = null;
        textFrequency = null;
    }

    public void incrementOccurrencesInText(int textNumber) {
        occurrencesByText.set(textNumber, occurrencesByText.get(textNumber) + 1);
    }

    public void calculateTotalStats(int wordsInCorpora) {
        double sum = 0;
        double textsWithOccurrences = 0;
        for (Integer d : occurrencesByText) {
            if (d > 0) {
                textsWithOccurrences += 1.0;
            }
            sum += (double) d;
        }
        frequency = sum / (double) wordsInCorpora;
        textFrequency = textsWithOccurrences/texts;
    }

    public Double getFrequency() {
        return frequency;
    }

    public Double getTextFrequency() {
        return textFrequency;
    }

    @Override
    public String toString() {
        DecimalFormat formatter = new DecimalFormat("#0.000");
        return "{frequency = " + formatter.format(frequency*100) + "%, text frequency = " + formatter.format(textFrequency*100) + "%}";
    }
}
