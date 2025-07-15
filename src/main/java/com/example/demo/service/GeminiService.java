package com.example.demo.service;

import com.example.demo.model.dto.GeminiRequestWithContext;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class GeminiService {
    private final RestTemplate restTemplate = new RestTemplate();

    public String generateResponseWithContext(GeminiRequestWithContext request) {
        List<Map<String, String>> messages = request.getMessages();
        String systemPrompt = request.getSystemPrompt();

        List<Map<String, Object>> contents = new ArrayList<>();

        for (Map<String, String> message : messages) {
            Map<String, Object> part = Map.of("text", message.get("content"));
            Map<String, Object> content = Map.of(
                    "role", message.get("role"),
                    "parts", List.of(part)
            );
            contents.add(content);
        }

        Map<String, Object> body = Map.of(
                "contents", contents,
                "systemInstruction", Map.of("parts", List.of(Map.of("text", systemPrompt)))
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=AIzaSyB-5unGKac-ghdw2BNg62zKp_1yMIYStxE";

        ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> responseBody = response.getBody();
            List<Map<String, Object>> candidates = (List<Map<String, Object>>) responseBody.get("candidates");
            Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
            List<Map<String, String>> parts = (List<Map<String, String>>) content.get("parts");
            return parts.get(0).get("text");
        }

        return "Contextual explanation not available.";
    }
}