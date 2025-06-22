package com.hanati.cop.tuneWizard.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class RAGServerRequestDTO {
    String query;

    @Builder
    RAGServerRequestDTO(String query) {
        this.query = query;
    }
}
