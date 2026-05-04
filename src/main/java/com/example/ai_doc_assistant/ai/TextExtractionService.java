package com.example.ai_doc_assistant.ai;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class TextExtractionService {
    private final Tika tika = new Tika();
    public String extractText(MultipartFile file) throws TikaException, IOException {
        try{
            return tika.parseToString(file.getInputStream());
        }catch(Exception e){
            throw new RuntimeException("Failed to extract text", e);
        }
    }
}
