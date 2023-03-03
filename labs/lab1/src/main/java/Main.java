package main.java;

import javax.xml.stream.XMLStreamException;
import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) {
        /*ArrayList<String> words = WordParser.parseRussianWordsFromCorpora(
                Objects.requireNonNull(Main.class.getClassLoader().getResourceAsStream("output.txt")));
        System.out.println(words.size());*/

        String[] strings = new String[]{"пельменя", "пельменю", "картошкой", "кушала", "бумажные"};

        Lemmatizer lemmatizer;
        try {
            lemmatizer = new Lemmatizer("dict.opcorpora");
            for (String s : strings) {
                System.out.println("Original: " + s + " --> Lemma: " + lemmatizer.findLemmaForm(s));
            }
        } catch (FileNotFoundException | XMLStreamException e) {
            e.printStackTrace();
        }
    }
}