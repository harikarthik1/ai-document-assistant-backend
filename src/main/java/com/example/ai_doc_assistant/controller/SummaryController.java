package com.example.ai_doc_assistant.controller;

import com.example.ai_doc_assistant.dto.SummaryResponse;
import com.example.ai_doc_assistant.service.SummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/documents")
@RequiredArgsConstructor
public class SummaryController {

    private final SummaryService summaryService;

    @PostMapping("/{id}/summary")
    public SummaryResponse summarizeDocument(@PathVariable Long id) {

        String summary = summaryService.summarizeDocument(id);

        return new SummaryResponse(summary);
    }
}