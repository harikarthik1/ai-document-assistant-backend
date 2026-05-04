package com.example.ai_doc_assistant.controller;

import com.example.ai_doc_assistant.entity.ChatHistory;
import com.example.ai_doc_assistant.entity.User;
import com.example.ai_doc_assistant.repository.ChatHistoryRepository;
import com.example.ai_doc_assistant.repository.UserRepository;
import com.example.ai_doc_assistant.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatHistoryRepository chatRepository;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @DeleteMapping("/{documentId}")
    public void deleteAllChats(
            @PathVariable Long documentId,
            @RequestHeader("Authorization") String authHeader
    ) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Invalid Authorization header");
        }

        String token = authHeader.substring(7);
        String email = jwtUtil.extractEmail(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<ChatHistory> chats =
                chatRepository.findByUserIdAndDocumentIdOrderByCreatedAtAsc(
                        user.getId(), documentId
                );

        chatRepository.deleteAll(chats);
    }

    @GetMapping("/{documentId}")
    public List<ChatHistory> getChats(
            @PathVariable Long documentId,
            @RequestHeader("Authorization") String authHeader
    ) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Invalid Authorization header");
        }

        String token = authHeader.substring(7);
        String email = jwtUtil.extractEmail(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return chatRepository.findByUserIdAndDocumentIdOrderByCreatedAtAsc(user.getId(), documentId);
    }
}