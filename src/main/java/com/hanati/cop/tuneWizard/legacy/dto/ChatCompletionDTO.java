package com.hanati.cop.tuneWizard.legacy.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChatCompletionDTO {
    private String model;
    private List<ChatRequestMsgDTO> messages;

    @Builder
    public ChatCompletionDTO(String model, List<ChatRequestMsgDTO> messages) {
        this.model = model;
        this.messages = messages;
    }
}
