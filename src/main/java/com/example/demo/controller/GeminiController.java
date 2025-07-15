package com.example.demo.controller;

import com.example.demo.model.dto.GeminiRequestWithContext;
import com.example.demo.service.GeminiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class GeminiController {
    private final GeminiService geminiService;

    public GeminiController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    @PostMapping("/explainWithContext")
    public ResponseEntity<Map<String, String>> explainWithContext(@RequestBody GeminiRequestWithContext request) {
        String result = geminiService.generateResponseWithContext(request);
        Map<String, String> response = new HashMap<>();
        response.put("summary", result);
        return ResponseEntity.ok(response);
    }
}
