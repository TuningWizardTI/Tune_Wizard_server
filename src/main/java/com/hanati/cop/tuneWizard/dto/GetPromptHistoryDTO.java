package com.hanati.cop.tuneWizard.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class GetPromptHistoryDTO {

    String user_id;
    @Builder
    GetPromptHistoryDTO(String user_id) {
        this.user_id = user_id;
    }
}
