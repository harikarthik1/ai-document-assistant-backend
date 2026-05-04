package com.example.ai_doc_assistant.service;

import com.example.ai_doc_assistant.ai.GeminiChatService;
import com.example.ai_doc_assistant.entity.DocumentChunk;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.ai_doc_assistant.repository.ChatHistoryRepository;
import com.example.ai_doc_assistant.entity.ChatHistory;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RagService {

    private final SemanticSearchService semanticSearchService;
    private final GeminiChatService geminiChatService;
    private final ChatHistoryRepository chatRepository;
    public String askQuestion(Long documentId, Long userId, String question) {

        // 1. Get previous chats (memory)
        List<ChatHistory> history =
                chatRepository.findByUserIdAndDocumentIdOrderByCreatedAtAsc(userId, documentId);

        StringBuilder memory = new StringBuilder();

        for (ChatHistory chat : history) {
            memory.append("Q: ").append(chat.getQuestion()).append("\n");
            memory.append("A: ").append(chat.getAnswer()).append("\n\n");
        }

        // 2. Get relevant chunks
        List<DocumentChunk> chunks =
                semanticSearchService.search(documentId, question);

        StringBuilder contextBuilder = new StringBuilder();

        for (DocumentChunk chunk : chunks) {
            contextBuilder.append(chunk.getContent()).append("\n\n");
        }

        // 3. Combine memory + context
        String finalContext = "Previous conversation:\n"
                + memory
                + "\nDocument context:\n"
                + contextBuilder;

        // 4. Call AI
        String answer = geminiChatService.generateAnswer(finalContext, question);

        // 5. Save chat
        ChatHistory chat = ChatHistory.builder()
                .userId(userId)
                .documentId(documentId)
                .question(question)
                .answer(answer)
                .createdAt(LocalDateTime.now())
                .build();

        chatRepository.save(chat);

        return answer;
    }
}
