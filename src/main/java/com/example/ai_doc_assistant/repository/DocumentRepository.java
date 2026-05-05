package com.example.ai_doc_assistant.repository;

import com.example.ai_doc_assistant.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByUserId(Long userId);
    java.util.Optional<Document> findByIdAndUserId(Long id, Long userId);
}
