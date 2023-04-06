package linguistic_labs.commons.freq;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class CsvFrequencyDictionaryReader {

    private final InputStream fileStream;

    public CsvFrequencyDictionaryReader(InputStream stream) {
        fileStream = stream;
    }

    public FrequencyDictionary parseFrequencies() {
        FrequencyDictionary frequencyDictionary = new FrequencyDictionary();

        try (CSVReader c = new CSVReaderBuilder(new InputStreamReader(fileStream))
                .withCSVParser(new CSVParserBuilder()
                        .withQuoteChar('\'')
                        .withSeparator('\t')
                        .build())
                .build())
        {
            List<String[]> rows = c.readAll();
            for (String[] row : rows.subList(1,rows.size())) {
                frequencyDictionary.add(row[0], new FrequencyDictionaryEntry(row[1], Double.valueOf(row[2])));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return frequencyDictionary;
    }
}
