package com.example.ai_doc_assistant.service;

import com.example.ai_doc_assistant.ai.CosineSimilarity;
import com.example.ai_doc_assistant.ai.EmbeddingService;
import com.example.ai_doc_assistant.entity.DocumentChunk;
import com.example.ai_doc_assistant.repository.DocumentChunkRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SemanticSearchService {

    private final DocumentChunkRepository chunkRepository;
    private final EmbeddingService embeddingService;
    private final ObjectMapper objectMapper;

    public List<DocumentChunk> search(Long documentId, String question) {

        List<DocumentChunk> chunks = chunkRepository.findByDocumentId(documentId);

        List<Double> questionEmbedding =
                embeddingService.generateEmbedding(question);

        return chunks.stream()
                .sorted((a, b) -> Double.compare(
                        similarity(b, questionEmbedding),
                        similarity(a, questionEmbedding)))
                .limit(5)
                .toList();
    }

    private double similarity(DocumentChunk chunk, List<Double> questionEmbedding) {

        try {

            List<Double> chunkEmbedding =
                    objectMapper.readValue(chunk.getEmbedding(), List.class);

            return CosineSimilarity.calculate(chunkEmbedding, questionEmbedding);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}