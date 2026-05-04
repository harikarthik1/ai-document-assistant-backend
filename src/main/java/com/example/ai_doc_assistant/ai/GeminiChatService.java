package com.example.ai_doc_assistant.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class GeminiChatService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private final ObjectMapper objectMapper;

    private final RestTemplate restTemplate = new RestTemplate();

    public String generateAnswer(String context, String question) {

        try {

            String url =
                    "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash-lite:generateContent";

            String prompt = """
                    Use the context below to answer the question.

                    Context:
                    %s

                    Question:
                    %s

                    Answer clearly based only on the context.
                    Do not use markdown formatting, bullets, or numbered lists.
                    Keep the answer concise and in plain text.
                    """.formatted(context, question);

            String body = """
                    {
                      "contents":[
                        {
                          "parts":[
                            {"text":"%s"}
                          ]
                        }
                      ]
                    }
                    """.formatted(prompt.replace("\"",""));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("x-goog-api-key", apiKey);

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

            String text = json
                    .get("candidates")
                    .get(0)
                    .get("content")
                    .get("parts")
                    .get(0)
                    .get("text")
                    .asText();

            return sanitizeAnswer(text);

        } catch (Exception e) {

            throw new RuntimeException("Gemini generation failed", e);

        }
    }

    private String sanitizeAnswer(String text) {
        if (text == null) {
            return "";
        }
        String cleaned = text
                .replaceAll("\\*\\*", "")
                .replaceAll("(?m)^\\s*[-*+]\\s+", "")
                .replaceAll("(?m)^\\s*\\d+\\.\\s+", "")
                .replaceAll("\\r\\n", "\\n")
                .replaceAll("\\n{3,}", "\\n\\n")
                .trim();
        return cleaned;
    }
}