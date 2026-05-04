package com.example.ai_doc_assistant.service;

import com.example.ai_doc_assistant.ai.GeminiSummaryService;
import com.example.ai_doc_assistant.entity.Document;
import com.example.ai_doc_assistant.entity.DocumentChunk;
import com.example.ai_doc_assistant.repository.DocumentChunkRepository;
import com.example.ai_doc_assistant.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SummaryService {

    private final DocumentRepository documentRepository;
    private final DocumentChunkRepository chunkRepository;
    private final GeminiSummaryService geminiSummaryService;

    public String summarizeDocument(Long documentId) {

        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        List<DocumentChunk> chunks =
                chunkRepository.findByDocumentId(documentId);

        StringBuilder content = new StringBuilder();

        for (DocumentChunk chunk : chunks) {
            content.append(chunk.getContent()).append("\n");
        }

        if (document.getSummary() != null) {
            return document.getSummary();
        }

        String summary = geminiSummaryService.generateSummary(content.toString());

        document.setSummary(summary);
        documentRepository.save(document);

        return summary;
    }
}