package com.example.ai_doc_assistant.repository;

import com.example.ai_doc_assistant.entity.ChatHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatHistoryRepository extends JpaRepository<ChatHistory, Long> {

    List<ChatHistory> findByUserIdAndDocumentIdOrderByCreatedAtAsc(
            Long userId, Long documentId
    );
}