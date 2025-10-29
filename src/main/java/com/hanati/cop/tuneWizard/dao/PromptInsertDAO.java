package com.hanati.cop.tuneWizard.dao;

import lombok.Data;

@Data
public class PromptInsertDAO {
    private String uuid;
    private String prompt_text;
    private String response_text;
    private String user_id;
    private String model_name;
    private String call_date;
    private String call_time;

}
