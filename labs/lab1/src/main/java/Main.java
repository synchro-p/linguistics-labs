import javax.xml.stream.XMLStreamException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        ArrayList<String> words = WordParser.parseRussianWordsFromCorpora(
                Objects.requireNonNull(Main.class.getClassLoader().getResourceAsStream("corpora/samoseyko/output.txt")));
        System.out.println(words.size());
        startup(400, "corpora/samoseyko/", ".txt");
    }

    private static void startup(int textsInCorpora, String prefix, String postfix) {
        int sum = 0;
        for (int i = 0; i < textsInCorpora; i++) {
            String filenameBuilder = prefix + i + postfix;
            ArrayList<String> words = WordParser.parseRussianWordsFromCorpora(
                Objects.requireNonNull(Main.class.getClassLoader().getResourceAsStream(filenameBuilder)));

        }
    }
}