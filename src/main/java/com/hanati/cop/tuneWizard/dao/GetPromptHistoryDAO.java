package com.hanati.cop.tuneWizard.dao;

import lombok.Data;

@Data
public class GetPromptHistoryDAO {
    private String uuid;
    private String promptText;
    private String responseText;
    private String modelName;
    private String callDate;
    private String callTime;
    private String userId;
}
