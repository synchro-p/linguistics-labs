import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Pattern;

public class WordParser {
    public static ArrayList<String> parseRussianWordsFromCorpora(InputStream corpora) {
        Scanner scanner = new Scanner(corpora).useDelimiter("\\p{Punct}*\\s+");
        Locale rusLocale = new Locale("ru");

        Pattern rusWordPattern = Pattern.compile("\\p{InCYRILLIC}+(-\\p{InCYRILLIC}+)?");

        ArrayList<String> words = new ArrayList<>();

        while (scanner.hasNext()) {
            String s = scanner.next();
            if (!s.isEmpty() && rusWordPattern.matcher(s).matches()) {
                if (s.contains("ё")) {
                    System.out.println("Не зря");
                }
                words.add(s.toLowerCase(rusLocale).replace('ё', 'е'));
            }
        }
        return words;
    }
}
