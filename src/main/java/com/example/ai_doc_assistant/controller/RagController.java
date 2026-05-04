package com.example.ai_doc_assistant.controller;

import com.example.ai_doc_assistant.dto.AskQuestionRequest;
import com.example.ai_doc_assistant.dto.AskQuestionResponse;
import com.example.ai_doc_assistant.entity.User;
import com.example.ai_doc_assistant.repository.UserRepository;
import com.example.ai_doc_assistant.security.JwtUtil;
import com.example.ai_doc_assistant.service.RagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/documents")
@RequiredArgsConstructor
public class RagController {

    private final RagService ragService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @PostMapping("/{id}/ask")
    public AskQuestionResponse askQuestion(
            @PathVariable Long id,
            @RequestBody AskQuestionRequest request,
            @RequestHeader("Authorization") String authHeader
    ) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Invalid Authorization header");
        }

        String token = authHeader.substring(7);
        String email = jwtUtil.extractEmail(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String answer = ragService.askQuestion(
                id,
                user.getId(),
                request.getQuestion()
        );

        return new AskQuestionResponse(answer);
    }
}