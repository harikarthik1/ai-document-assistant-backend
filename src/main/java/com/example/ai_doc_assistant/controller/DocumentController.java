package com.example.ai_doc_assistant.controller;

import com.example.ai_doc_assistant.entity.Document;
import com.example.ai_doc_assistant.entity.User;
import com.example.ai_doc_assistant.repository.UserRepository;
import com.example.ai_doc_assistant.security.JwtUtil;
import com.example.ai_doc_assistant.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.apache.tika.exception.TikaException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/documents")
@RequiredArgsConstructor
public class DocumentController {
    private final DocumentService documentService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public Document uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestHeader("Authorization") String authHeader
    ) throws IOException, TikaException {

        String token = authHeader.substring(7);
        String email = jwtUtil.extractEmail(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return documentService.uploadDocument(file, user.getId());
    }

    @GetMapping
    public List<Document> getDocuments(
            @RequestHeader("Authorization") String authHeader
    ){
        String token = authHeader.substring(7);
        String email = jwtUtil.extractEmail(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return documentService.getUserDocuments(user.getId());
    }
}
