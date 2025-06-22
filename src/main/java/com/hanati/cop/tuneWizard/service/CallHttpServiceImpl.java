package com.hanati.cop.tuneWizard.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanati.cop.tuneWizard.config.HttpCallConfig;
import com.hanati.cop.tuneWizard.dto.ChatCompletionDTO;
import com.hanati.cop.tuneWizard.dto.RAGServerRequestDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@Service
public class CallHttpServiceImpl implements CallHttpService{

    public HttpCallConfig httpCallConfig;

    @Value("${flask.url}")
    private String url;

    @Override
   public Map<String, Object> CallFlaskLLM (RAGServerRequestDTO rag) {
        log.debug("[+] 신규 프롬포트를 수행한다.");

        Map<String, Object> resultMap = new HashMap<>();

        // [STEP1] 토큰 정보가 포함된 Header를 가져옴
        HttpHeaders headers = httpCallConfig.httpHeaders();

        // [STEP5] 통신을 위한 RestTemplate을 구성함
        HttpEntity<RAGServerRequestDTO> requestEntity = new HttpEntity<>(rag, headers);

        ResponseEntity<String> response = httpCallConfig
                .restTemplate()
                .exchange(url
                        , HttpMethod.POST
                        , requestEntity
                        , String.class);

        try {
            // [STEP6] String -> HashMap 역직렬화 구성
            ObjectMapper om = new ObjectMapper();
            resultMap = om.readValue(response.getBody(), new TypeReference<>(){});
        }catch (JsonProcessingException e) {
            e.getStackTrace();
            log.debug("JsonMappingException ::"  + e.getMessage());
        }catch (RuntimeException e) {
            e.getStackTrace();
        }

        return null;
    }
}
