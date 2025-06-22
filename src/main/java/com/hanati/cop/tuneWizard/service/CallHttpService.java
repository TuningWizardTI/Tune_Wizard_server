package com.hanati.cop.tuneWizard.service;


import com.hanati.cop.tuneWizard.dto.RAGServerRequestDTO;

import java.util.Map;

public interface CallHttpService {
    Map<String, Object>  CallFlaskLLM (RAGServerRequestDTO rag) ;

}
