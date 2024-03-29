package linguistic_labs.lab1;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Pattern;

public class WordParser {
    public static ArrayList<String> parseRussianWordsFromCorpora(InputStream textFile) {
        Scanner scanner = new Scanner(textFile).useDelimiter("\\p{Punct}*\\s+");
        Locale rusLocale = new Locale("ru");

        Pattern rusWordPattern = Pattern.compile("\\p{InCYRILLIC}+(-\\p{InCYRILLIC}+)?");

        ArrayList<String> words = new ArrayList<>();

        while (scanner.hasNext()) {
            String s = scanner.next();
            if (!s.isEmpty() && rusWordPattern.matcher(s).matches()) {
                words.add(s.toLowerCase(rusLocale).replace('ё', 'е'));
            }
        }
        return words;
    }
}
