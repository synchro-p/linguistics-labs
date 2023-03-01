import java.util.ArrayList;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        ArrayList<String> words = WordParser.parseRussianWordsFromCorpora(
                Objects.requireNonNull(Main.class.getClassLoader().getResourceAsStream("output.txt")));
        System.out.println(words.size());
    }
}