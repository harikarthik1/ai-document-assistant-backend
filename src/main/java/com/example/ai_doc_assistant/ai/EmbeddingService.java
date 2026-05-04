package com.example.ai_doc_assistant.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmbeddingService {

    private final ObjectMapper objectMapper;

    private final RestTemplate restTemplate = new RestTemplate();
    @Value("${gemini.api.key}")
    private String API_KEY;

    public List<Double> generateEmbedding(String text) {

        try {

            String url =
                    "https://generativelanguage.googleapis.com/v1beta/models/gemini-embedding-001:embedContent";

            String body = """
                    {
                      "model": "models/gemini-embedding-001",
                      "content": {
                        "parts": [
                          {"text": "%s"}
                        ]
                      }
                    }
                    """.formatted(text.replace("\"",""));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("x-goog-api-key", API_KEY);

            HttpEntity<String> request =
                    new HttpEntity<>(body, headers);

            ResponseEntity<String> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.POST,
                            request,
                            String.class
                    );

            JsonNode json = objectMapper.readTree(response.getBody());

            JsonNode vector =
                    json.get("embedding").get("values");

            return objectMapper.convertValue(vector, List.class);

        } catch (Exception e) {
            throw new RuntimeException("Embedding generation failed", e);
        }
    }
}