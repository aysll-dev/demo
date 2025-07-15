package com.example.demo.controller;

import com.example.demo.service.SummaryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class SummaryController {

    private final SummaryService summaryService;

    public SummaryController(SummaryService summaryService) {
        this.summaryService = summaryService;
    }

    @PostMapping(value = "/summarize", produces = "application/json")
    public ResponseEntity<Map<String, String>> summarize(@RequestBody Map<String, String> request) {
        try {
            String content = request.get("content");
            String summaryType = request.getOrDefault("summaryType", "short");

            String summary = summaryService.summarize(content);
            return ResponseEntity.ok(Map.of("summary", summary));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping(value = "/explain", produces = "application/json")
    public ResponseEntity<Map<String, String>> explain(@RequestBody Map<String, String> request) {
        try {
            String topic = request.get("topic");
            String explanation = summaryService.explainTopic(topic);
            return ResponseEntity.ok(Map.of("explanation", explanation));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }
}
