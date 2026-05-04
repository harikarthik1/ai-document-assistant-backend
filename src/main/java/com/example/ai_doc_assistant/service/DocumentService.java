package com.example.ai_doc_assistant.service;

import com.example.ai_doc_assistant.ai.TextExtractionService;
import com.example.ai_doc_assistant.entity.Document;
import com.example.ai_doc_assistant.repository.DocumentRepository;
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

}
