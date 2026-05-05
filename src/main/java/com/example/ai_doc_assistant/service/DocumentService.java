package com.example.ai_doc_assistant.service;

import com.example.ai_doc_assistant.ai.TextExtractionService;
import com.example.ai_doc_assistant.entity.Document;
import com.example.ai_doc_assistant.repository.ChatHistoryRepository;
import com.example.ai_doc_assistant.repository.DocumentChunkRepository;
import com.example.ai_doc_assistant.repository.DocumentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.tika.exception.TikaException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentService {
    private final DocumentRepository documentRepository;
    private final DocumentChunkRepository chunkRepository;
    private final ChatHistoryRepository chatHistoryRepository;
    private final String uploadDir = "uploads/";
    private final TextExtractionService textExtractionService;
    private final ChunkService chunkService;

    public Document uploadDocument(MultipartFile file, Long userId) throws IOException, TikaException {
        String extractedText = textExtractionService.extractText(file);

        Document document = Document.builder()
                .fileName(file.getOriginalFilename())
                .uploadTime(LocalDateTime.now())
                .userId(userId)
                .extractedText(extractedText)
                .build();

        Document savedDocument = documentRepository.save(document);

        chunkService.createChunks(savedDocument.getId(), extractedText);

        return savedDocument;
    }
    public List<Document> getUserDocuments(Long userId){
        return documentRepository.findByUserId(userId);
    }

    @Transactional
    public void deleteDocument(Long documentId, Long userId) {
        Document document = documentRepository.findByIdAndUserId(documentId, userId)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        chunkRepository.deleteByDocumentId(documentId);
        chatHistoryRepository.deleteByUserIdAndDocumentId(userId, documentId);
        documentRepository.delete(document);
    }

}
