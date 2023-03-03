package main.java;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lemmatizer {
    private final String dictionaryName;
    private final Map<Integer, Lemma> lemmasDict;

    public Lemmatizer(String dictionaryName) throws XMLStreamException, FileNotFoundException {
        this.dictionaryName = dictionaryName;
        lemmasDict = new HashMap<>();
        loadLemmas();
    }

    public void loadLemmas() throws FileNotFoundException, XMLStreamException {
        XMLInputFactory streamFactory = XMLInputFactory.newInstance();
        XMLEventReader reader = streamFactory.createXMLEventReader(
                new FileInputStream("labs/lab1/src/main/resources/" + dictionaryName + ".xml"));
        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                if (startElement.getName().getLocalPart().equals("lemma")) {
                    int id = Integer.parseInt(startElement.getAttributeByName(new QName("id")).getValue());
                    List<String> forms = new ArrayList<>();
                    String lemmaForm = null;

                    boolean flag = true;
                    while (flag) {
                        event = reader.nextEvent();

                        if (event.isStartElement()) {
                            startElement = event.asStartElement();
                            if (startElement.getName().getLocalPart().equals("l")) {
                                lemmaForm = startElement.getAttributeByName(new QName("t")).getValue();
                            } else if (startElement.getName().getLocalPart().equals("f")) {
                                forms.add(startElement.getAttributeByName(new QName("t")).getValue());
                            }
                        }
                        if (event.isEndElement()) {
                            if (event.asEndElement().getName().getLocalPart().equals("lemma")) {
                                flag = false;
                            }
                        }
                    }

                    lemmasDict.put(id, new Lemma(lemmaForm, forms));
                }
            }
        }
    }

    public Lemma findLemma(String word) {
        for (Lemma lemma : lemmasDict.values()) {
            for (String s : lemma.getWordForms()) {
                if (s.equals(word))
                    return lemma;
            }
        }
        return null;
    }

    public String findLemmaForm(String word) {
        Lemma lemma = findLemma(word);
        if (lemma != null) return lemma.getLemmaForm();
        else return null;
    }
}
