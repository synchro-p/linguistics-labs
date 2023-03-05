import com.sun.tools.javac.Main;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileNotFoundException;
import java.util.*;

public class Lemmatizer {
    private final String dictionaryName;
    private final Trie lemmaTrie;

    public Lemmatizer(String dictionaryName) throws XMLStreamException, FileNotFoundException {
        this.dictionaryName = dictionaryName;
        lemmaTrie = new Trie();
        loadLemmas();
    }

    public void loadLemmas() throws XMLStreamException {
        XMLInputFactory streamFactory = XMLInputFactory.newInstance();
        XMLEventReader reader = streamFactory.createXMLEventReader(
                Objects.requireNonNull(Main.class.getClassLoader().getResourceAsStream(dictionaryName)));
        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                if (startElement.getName().getLocalPart().equals("lemma")) {
                    int id = Integer.parseInt(startElement.getAttributeByName(new QName("id")).getValue());
                    List<String> forms = new ArrayList<>();
                    List<String> grammemes = new ArrayList<>();
                    String lemmaForm = null;

                    boolean flag = true;
                    while (flag) {
                        event = reader.nextEvent();

                        if (event.isStartElement()) {
                            startElement = event.asStartElement();
                            switch (startElement.getName().getLocalPart()) {
                                case "l" -> {
                                    lemmaForm = startElement.getAttributeByName(new QName("t")).getValue().replace("ё", "е");
                                    boolean flag2 = true;
                                    while (flag2) {
                                        event = reader.nextEvent();

                                        if (event.isEndElement()) {
                                            if (event.asEndElement().getName().getLocalPart().equals("l"))
                                                flag2 = false;
                                        }

                                        if (event.isStartElement()) {
                                            if (event.asStartElement().getName().getLocalPart().equals("g")) {
                                                grammemes.add(event.asStartElement().getAttributeByName(new QName("v")).getValue());
                                            }
                                        }
                                    }
                                }
                                case "f" -> forms.add(startElement.getAttributeByName(new QName("t")).getValue().replace("ё", "е"));
                            }
                        }
                        if (event.isEndElement()) {
                            if (event.asEndElement().getName().getLocalPart().equals("lemma")) {
                                flag = false;
                            }
                        }
                    }

                    Lemma newLemma = new Lemma(lemmaForm, grammemes);
                    for (String form : forms) {
                        lemmaTrie.add(form, newLemma);
                    }
                }
            }
        }
    }

    public ArrayList<Lemma> findLemmas(String wordForm) {
        return lemmaTrie.getLemmasForWord(wordForm);
    }
}
