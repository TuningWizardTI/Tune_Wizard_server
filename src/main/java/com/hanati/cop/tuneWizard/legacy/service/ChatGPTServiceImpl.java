package com.hanati.cop.tuneWizard.legacy.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanati.cop.tuneWizard.config.ChatGPTConfig;
import com.hanati.cop.tuneWizard.legacy.dto.ChatCompletionDTO;
import com.hanati.cop.tuneWizard.legacy.dto.CompletionRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ChatGPTServiceImpl implements ChatGPTService{
    private final ChatGPTConfig chatGPTConfig;

    @Value("${openai.url.model}")
    private String modelUrl;

    @Value("${openai.url.model-list}")
    private String modelListUrl;

    @Value("${openai.url.prompt}")
    private String promptUrl;

    @Value("${openai.url.legacy_promt}")
    private String legacyPromptUrl;

    public ChatGPTServiceImpl(ChatGPTConfig chatGPTConfig) {
        this.chatGPTConfig = chatGPTConfig;
    }

    /*
    * 사용 가능한 모델 리스트를 조회하는 비즈니스 로직
    *
    * @return List<Map <String, Object>>
    * */
    @Override
    public List<Map<String, Object>> modelList(){
        log.debug("[+] 모델 리스트를 조회한다.");
        List<Map<String, Object>> resultList = null;

        HttpHeaders headers = chatGPTConfig.httpHeaders();

        ResponseEntity<String> response = chatGPTConfig
                .restTemplate()
                .exchange(
                        modelUrl,
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        String.class
                );

        try{
            // [step 3] Jackson을 기반으로 응답값을 가져옴
            ObjectMapper om = new ObjectMapper();
            List<Map<String, Object>> data = om.readValue(response.getBody(), new TypeReference<>(){});
            // [STEP4] 응답 값을 결과값에 넣고 출력을 해봅니다.
            resultList = (List<Map<String, Object>>) data;

            for (Map<String, Object> object : resultList) {
                log.debug("ID: " + object.get("id"));
                log.debug("Object: " + object.get("object"));
                log.debug("Created: " + object.get("created"));
                log.debug("Owned By: " + object.get("owned_by"));
            }
        }catch(JsonMappingException e){
            log.debug("JsonMappingException :: " + e.getMessage());
        }catch(JsonProcessingException e){
            log.debug("RuntimeException :: " + e.getMessage());
        }catch(RuntimeException e){
            log.debug("RuntimeException :: " + e.getMessage());
        }

        return resultList;

    }
    @Override
    public Map<String, Object> isValidModel(String modelName) {
        log.debug("[+] 모델이 유효한지 조회한다.");
        Map<String, Object> result = new HashMap<>();

        // [STEP1] 토큰 정보가 포함된 Header를 가져옴
        HttpHeaders headers = chatGPTConfig.httpHeaders();

        // [STEP2] 통신을 위한 RestTemplate을 구성한다
        ResponseEntity<String> response = chatGPTConfig
                .restTemplate()
                .exchange(
                        modelListUrl + "/" + modelName
                        ,HttpMethod.GET
                        ,new HttpEntity<>(headers)
                        ,String.class);

        try {
            // [STEP3] Jackson을 기반으로 응답값을 가져옴
            ObjectMapper om = new ObjectMapper();
            result = om.readValue(response.getBody(), new TypeReference<>() {
            });
        }catch (JsonProcessingException e) {
            log.debug("JsonMappingException :: " + e.getMessage());
        }catch(RuntimeException e) {
            log.debug("RuntimeException  :: " + e.getMessage());
        }
        return result;
    }

    @Override
    public Map<String, Object> prompt(ChatCompletionDTO chatCompletionDTO) {
        log.debug("[+] 신규 프롬포트를 수행한다.");

        Map<String, Object> resultMap = new HashMap<>();

        // [STEP1] 토큰 정보가 포함된 Header를 가져옴
        HttpHeaders headers = chatGPTConfig.httpHeaders();

        // [STEP5] 통신을 위한 RestTemplate을 구성함
        HttpEntity<ChatCompletionDTO> requestEntity = new HttpEntity<>(chatCompletionDTO, headers);

        System.out.println(requestEntity);
        ResponseEntity<String> response = chatGPTConfig
                .restTemplate()
                .exchange(promptUrl
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
        return resultMap;
    }

    @Override
    public Map<String, Object> legacyPrompt(CompletionRequestDTO completionRequestDTO) {
        log.debug("[+] 레거시 프롬프트를 수행합니다.");

        // [STEP1] 토큰 정보가 포함된 Header를 가져옵니다.
        HttpHeaders headers = chatGPTConfig.httpHeaders();

        // [STEP5] 통신을 위한 RestTemplate을 구성합니다.
        HttpEntity<CompletionRequestDTO> requestEntity = new HttpEntity<>(completionRequestDTO, headers);
        ResponseEntity<String> response = chatGPTConfig
                .restTemplate()
                .exchange(legacyPromptUrl, HttpMethod.POST, requestEntity, String.class);

        Map<String, Object> resultMap = new HashMap<>();
        try {
            ObjectMapper om = new ObjectMapper();
            // [STEP6] String -> HashMap 역직렬화를 구성합니다.
            resultMap = om.readValue(response.getBody(), new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            log.debug("JsonMappingException :: " + e.getMessage());
        } catch (RuntimeException e) {
            log.debug("RuntimeException :: " + e.getMessage());
        }
        return resultMap;
    }

}
