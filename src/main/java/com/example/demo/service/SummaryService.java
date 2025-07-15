package com.example.demo.service;

import com.example.demo.model.GeminiRequest;
import com.example.demo.model.GeminiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SummaryService {

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    public String summarize(String content){
        RestTemplate restTemplate=new RestTemplate();

        GeminiRequest geminiRequest=new GeminiRequest(List.of(
                new GeminiRequest.Content(List.of(
                        new GeminiRequest.Part("Summarize: \n"+content)
                ))
        ));



        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<GeminiRequest> entity=new HttpEntity<>(geminiRequest, httpHeaders);

        ResponseEntity<GeminiResponse> response=restTemplate.exchange(
                geminiApiUrl,
                HttpMethod.POST,
                entity,
                GeminiResponse.class
        );

        if(response.getStatusCode()==HttpStatus.OK &&
        response.getBody()!=null && !response.getBody().getCandidates().isEmpty()){
            return response.getBody()
                    .getCandidates()
                    .get(0)
                    .getContent()
                    .getParts()
                    .get(0)
                    .getText();
        }
        return "No summary";
    }

    public String explainTopic(String topic) {
        RestTemplate restTemplate = new RestTemplate();

        GeminiRequest request = new GeminiRequest(List.of(
                new GeminiRequest.Content(List.of(
                        new GeminiRequest.Part("Explain this topic simply: " + topic)
                ))
        ));

        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<GeminiRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<GeminiResponse> response = restTemplate.exchange(
                geminiApiUrl, HttpMethod.POST, entity, GeminiResponse.class
        );

        if (response.getStatusCode() == HttpStatus.OK &&
                response.getBody() != null &&
                !response.getBody().getCandidates().isEmpty()) {
            return response.getBody().getCandidates().get(0).getContent().getParts().get(0).getText();
        }

        return "No explanation found.";
    }
}