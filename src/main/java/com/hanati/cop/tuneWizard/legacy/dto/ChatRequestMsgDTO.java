package com.hanati.cop.tuneWizard.legacy.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/*
* author : jh
* filter : legacy
* */
@Getter
@Setter
public class ChatRequestMsgDTO {
    private String role;
    private String content;

    @Builder
    public ChatRequestMsgDTO(String role, String content) {
        this.role = role;
        this.content = content;
    }
}
