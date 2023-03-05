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
        boolean infNeeded = false;
        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                if (startElement.getName().getLocalPart().equals("lemma")) {
                    //int id = Integer.parseInt(startElement.getAttributeByName(new QName("id")).getValue());
                    Set<String> forms = new HashSet<>();
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
                                                String grammeme = event.asStartElement().getAttributeByName(new QName("v")).getValue();
                                                if (grammeme.equals("VERB")) {
                                                    //this is a verb lemma that should be replaced with infinitive lemma.
                                                    forms.add(lemmaForm);
                                                    infNeeded = true;
                                                }
                                                else {
                                                    grammemes.add(grammeme);
                                                }
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
                                if (infNeeded) {
                                    //read the infinitive version in next lemma
                                    boolean flag2 = true;
                                    while (flag2) {
                                        event = reader.nextEvent();
                                        if (event.isStartElement())
                                            if (event.asStartElement().getName().getLocalPart().equals("l"))
                                                flag2 = false;
                                    }
                                    lemmaForm = event.asStartElement().getAttributeByName(new QName("t")).getValue().replace("ё", "е");

                                    //read the grammemes
                                    flag2 = true;
                                    while (flag2) {
                                        event = reader.nextEvent();

                                        if (event.isEndElement()) {
                                            if (event.asEndElement().getName().getLocalPart().equals("l"))
                                                flag2 = false;
                                        }

                                        if (event.isStartElement()) {
                                            if (event.asStartElement().getName().getLocalPart().equals("g")) {
                                                String grammeme = event.asStartElement().getAttributeByName(new QName("v")).getValue();
                                                grammemes.add(grammeme);
                                            }
                                        }
                                    }
                                    infNeeded = false;
                                    flag = true;
                                }
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
