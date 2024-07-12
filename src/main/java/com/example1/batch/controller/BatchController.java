package com.example1.batch.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

@RestController
@RequestMapping("/api")
public class BatchController {
    @PostMapping("/convert")
    public ResponseEntity<JsonNode> convertXmlToJson(@RequestBody String xml) {
        XmlMapper xmlMapper = new XmlMapper();
        JsonNode jsonNode;
        try {
            System.out.println("Reached inside");
            jsonNode = xmlMapper.readTree(xml.getBytes());
        } catch (IOException e) {
            System.out.println("Error occured: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(jsonNode);
    }
}
