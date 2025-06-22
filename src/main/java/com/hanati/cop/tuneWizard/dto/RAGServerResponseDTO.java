package com.hanati.cop.tuneWizard.dto;


import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class RAGServerResponseDTO {
    String answer;

    @Builder
    RAGServerResponseDTO(String answer) {
        this.answer = answer;
    }
}
