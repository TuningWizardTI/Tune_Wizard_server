package com.hanati.cop.tuneWizard.dto;

import com.hanati.cop.tuneWizard.dao.ChatTableIndexListDAO;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class RAGServerRequestDTO {
    String query;
    String table;
    String indexList;
    @Builder
    RAGServerRequestDTO(String query, String table, String indexList) {
        this.query = query;
        this.table = table;
        this.indexList = indexList;
    }
}
