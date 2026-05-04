package com.example.ai_doc_assistant.service;

import com.example.ai_doc_assistant.ai.EmbeddingService;
import com.example.ai_doc_assistant.entity.DocumentChunk;
import com.example.ai_doc_assistant.repository.DocumentChunkRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChunkService {

    private final DocumentChunkRepository chunkRepository;
    private final EmbeddingService embeddingService;
    private final ObjectMapper objectMapper;

    private static final int CHUNK_SIZE = 600;

    public void createChunks(Long documentId, String text) {

        List<String> chunks = splitText(text);

        for (String chunk : chunks) {

            List<Double> embedding = embeddingService.generateEmbedding(chunk);

            try {

                String embeddingJson = objectMapper.writeValueAsString(embedding);

                DocumentChunk documentChunk = DocumentChunk.builder()
                        .documentId(documentId)
                        .content(chunk)
                        .embedding(embeddingJson)
                        .build();

                chunkRepository.save(documentChunk);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private List<String> splitText(String text) {
        List<String> chunks = new ArrayList<>();
        int start = 0;
        while (start < text.length()) {
            int end = Math.min(start + CHUNK_SIZE, text.length());
            chunks.add(text.substring(start, end));
            start = end;
        }
        return chunks;
    }
}