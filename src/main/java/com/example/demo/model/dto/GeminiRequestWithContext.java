package com.example.demo.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class GeminiRequestWithContext {
    private String systemPrompt;
    private List<Map<String, String>> messages;
}
