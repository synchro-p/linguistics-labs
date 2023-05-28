package linguistic_labs.commons.lemma;

import com.sun.tools.javac.Main;
import linguistic_labs.commons.trie.Trie;

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
    private final Trie<Lemma> lemmaTrie;

    public Lemmatizer(String dictionaryName) throws XMLStreamException, FileNotFoundException {
        this.dictionaryName = dictionaryName;
        lemmaTrie = new Trie<>();
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
                    Set<WordForm> forms = new HashSet<>();
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
                                                    forms.add(new WordForm(lemmaForm, null));
                                                    infNeeded = true;
                                                }
                                                else {
                                                    grammemes.add(grammeme);
                                                }
                                            }
                                        }
                                    }
                                }
                                case "f" -> {
                                    String form = startElement.getAttributeByName(new QName("t")).getValue().replace("ё", "е");
                                    String formCase = null;
                                    if (grammemes.contains("NOUN")) {
                                        boolean flag2 = true;
                                        while (flag2) {
                                            event = reader.nextEvent();

                                            if (event.isEndElement()) {
                                                if (event.asEndElement().getName().getLocalPart().equals("f"))
                                                    flag2 = false;
                                            }

                                            if (event.isStartElement()) {
                                                if (event.asStartElement().getName().getLocalPart().equals("g")) {
                                                    formCase = event.asStartElement().getAttributeByName(new QName("v")).getValue();
                                                }
                                            }
                                        }
                                    }
                                    forms.add(new WordForm(form, formCase));
                                }
                            }
                        }
                        if (event.isEndElement()) {
                            if (event.asEndElement().getName().getLocalPart().equals("lemma")) {
                                flag = false;
                                if (infNeeded) {
                                    //read the infinitive version in next lemmas
                                    grammemes = new ArrayList<>();
                                    while (!grammemes.contains("INFN")) {
                                        grammemes.clear();
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
                                    }
                                    infNeeded = false;
                                    flag = true;
                                }
                            }
                        }
                    }


                    if (grammemes.contains("NOUN")) {
                        for (WordForm form : forms) {
                            List<String> formGrammemes = new ArrayList<>(grammemes);
                            formGrammemes.add(form.formCase);
                            lemmaTrie.add(form.form, new Lemma(lemmaForm, formGrammemes));
                        }
                    }
                    else {
                        Lemma newLemma = new Lemma(lemmaForm, grammemes);
                        for (WordForm form : forms) {
                            lemmaTrie.add(form.form, newLemma);
                        }
                    }
                }
            }
        }
    }

    public ArrayList<Lemma> findLemmas(String wordForm) {
        return lemmaTrie.getResultsForWord(wordForm);
    }
}
