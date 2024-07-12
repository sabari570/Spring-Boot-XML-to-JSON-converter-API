package com.example1.batch;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

@Component
public class XmlToJsonConverter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void convertXmlToJson(String xml) throws XMLStreamException, IOException {
        JsonFactory jsonFactory = new JsonFactory();
        Writer jsonWriter = new FileWriter("output.json");
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(jsonWriter);

        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(new StringReader(xml));

        List<Recording> recordings = new ArrayList<>();
        Recording recording = null;
        String elementName = null;

        while (xmlStreamReader.hasNext()) {
            int event = xmlStreamReader.next();

            switch (event) {
                case XMLStreamConstants.START_ELEMENT:
                    elementName = xmlStreamReader.getLocalName();
                    if ("RECORDING".equals(elementName)) {
                        recording = new Recording();
                    }
                    break;
                case XMLStreamConstants.CHARACTERS:
                    String text = xmlStreamReader.getText().trim();
                    if (text.length() == 0) {
                        break;
                    }
                    switch (elementName) {
                        case "RECORDING-TITLE-COLLECTING-SOCIETY":
                            recording.setTitle(text.toUpperCase());
                            break;
                        case "MAIN-ARTIST-NAME-COLLECTING-SOCIETY":
                            recording.addArtist(text.toUpperCase());
                            break;
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    elementName = xmlStreamReader.getLocalName();
                    if ("RECORDING".equals(elementName)) {
                        recordings.add(recording);
                    }
                    break;
            }
        }

        objectMapper.writeValue(jsonGenerator, new Recordings(recordings));

        jsonWriter.close();
        jsonGenerator.close();
    }

    static class Recordings {
        private List<Recording> Recordings;

        public Recordings(List<Recording> recordings) {
            this.Recordings = recordings;
        }

        public List<Recording> getRecordings() {
            return Recordings;
        }

        public void setRecordings(List<Recording> recordings) {
            this.Recordings = recordings;
        }
    }

    static class Recording {
        private String Title;
        private List<String> MainArtists = new ArrayList<>();

        public String getTitle() {
            return Title;
        }

        public void setTitle(String title) {
            Title = title;
        }

        public List<String> getMainArtists() {
            return MainArtists;
        }

        public void addArtist(String artist) {
            MainArtists.add(artist);
        }
    }
}
