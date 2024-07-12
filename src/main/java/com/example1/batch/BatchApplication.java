package com.example1.batch;

import java.io.IOException;

import javax.xml.stream.XMLStreamException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@ComponentScan(basePackages = { "com.example1.batch" })
@RestController
public class BatchApplication {

	private XmlToJsonConverter converter = new XmlToJsonConverter();

	public BatchApplication(XmlToJsonConverter converter) {
		this.converter = converter;
	}

	public static void main(String[] args) {
		SpringApplication.run(BatchApplication.class, args);
		System.out.println("Starting...");
	}

	// API ENDPOINT: http://localhost:8080/convert
	// HEADER TYPE: Content-Type: application/xml
	// INPUT CONTENT: Inside input.xml file
	@PostMapping("/convert")
	public ResponseEntity<String> convertXmlToJson(@RequestBody String xml) throws XMLStreamException, IOException {
		try {
			converter.convertXmlToJson(xml);
			return ResponseEntity.ok("XML converted to JSON successfully and written to output.json");
		} catch (XMLStreamException | IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to convert XML to JSON: " + e.getMessage());
		}
	}

}
