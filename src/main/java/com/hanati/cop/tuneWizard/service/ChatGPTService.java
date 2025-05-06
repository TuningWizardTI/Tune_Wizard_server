package com.hanati.cop.tuneWizard.service;

import com.hanati.cop.tuneWizard.dto.ChatCompletionDTO;
import com.hanati.cop.tuneWizard.dto.CompletionRequestDTO;

import java.util.List;
import java.util.Map;

public interface ChatGPTService {
    List<Map<String, Object>> modelList();

    Map<String, Object> prompt(ChatCompletionDTO chatCompletionDTO);

    Map<String, Object> legacyPrompt(CompletionRequestDTO requestDTO);
    Map<String, Object> isValidModel (String modelName);

}
