package com.example.ai_doc_assistant.controller;

import com.example.ai_doc_assistant.entity.DocumentChunk;
import com.example.ai_doc_assistant.service.SemanticSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/documents")
@RequiredArgsConstructor
public class SearchController {
    private final SemanticSearchService semanticSearchService;
    @PostMapping("/{id}/search")
    public List<DocumentChunk> search(@PathVariable Long id, @RequestBody Map<String, String> body){
        String question = body.get("question");
        return semanticSearchService.search(id, question);
    }
}
