package com.hanati.cop.tuneWizard.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class TableInfoListRequestDTO {

    String tableName;
    @Builder

    TableInfoListRequestDTO(String tableName) {
        this.tableName = tableName;
    }
}
