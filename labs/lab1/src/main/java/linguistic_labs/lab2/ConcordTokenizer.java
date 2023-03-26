package linguistic_labs.lab2;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Pattern;

public class ConcordTokenizer {
    private final List<String> words;

    public ConcordTokenizer() {
        this.words = new ArrayList<>();
    }

    public void tokenizeText(InputStream text) {
        Scanner scanner = new Scanner(text).useDelimiter("\\p{Punct}*\\s+");
        Locale rusLocale = new Locale("ru");

        Pattern rusWordPattern = Pattern.compile("\\p{InCYRILLIC}+(-\\p{InCYRILLIC}+)?");

        while (scanner.hasNext()) {
            String s = scanner.next();
            System.out.println(s);
            if (!s.isEmpty() && rusWordPattern.matcher(s).matches()) {
                words.add(s.toLowerCase(rusLocale).replace('ё', 'е'));
            }
        }
    }

    public List<String> getWords() {
        return words;
    }
}
