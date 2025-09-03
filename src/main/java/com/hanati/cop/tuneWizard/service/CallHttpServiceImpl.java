package com.hanati.cop.tuneWizard.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanati.cop.tuneWizard.config.ChatGPTConfig;
import com.hanati.cop.tuneWizard.dao.ChatTableIndexListDAO;
import com.hanati.cop.tuneWizard.dto.RAGServerRequestDTO;
import com.hanati.cop.tuneWizard.mapper.TITuneMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Service
public class CallHttpServiceImpl implements CallHttpService{

    public ChatGPTConfig httpCallConfig;
    @Autowired
    public TITuneMapper mapperClass;
    public CallHttpServiceImpl(ChatGPTConfig httpCallConfig) {
        this.httpCallConfig = httpCallConfig;
    }
    @Value("${flask.url}")
    private String url;

    @Override
   public Map<String, Object> CallFlaskLLM (RAGServerRequestDTO rag) {
        log.debug("[+] 신규 프롬포트를 수행한다.");

        Map<String, Object> resultMap = new HashMap<>();
        String table = rag.getTable();
        //인덱스 데이터 조회하여 세팅하기
        List<ChatTableIndexListDAO> list = mapperClass.tableIndexList(table);
        String startStringPrompt = "인덱스 구성은 다음과 같습니다. \n";
        for(int i = 0; i<list.size(); i++) {
            String indexName = list.get(i).getINDEX_NAME();
            String columnName = list.get(i).getCOLUMN_NAME();

            String totalStringPrompt = "인덱스의 이름 : " + indexName + " 인덱스의 구성 컬럼 " + columnName + "\n";
            System.out.println(totalStringPrompt);
            startStringPrompt += totalStringPrompt;
        }
        System.out.println(startStringPrompt);
        if("---".equals(table) || "".equals(table)){
            rag.setIndexList("");
        }else {
            rag.setIndexList(startStringPrompt);
        }

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

            String resultJson = om.writeValueAsString(resultMap);
            System.out.println(rag.getQuery());
            //발라낼 응답 미리 빼두기
            System.out.println(resultMap.get("answer"));

        }catch (JsonProcessingException e) {
            e.getStackTrace();
            log.debug("JsonMappingException ::"  + e.getMessage());
        }catch (RuntimeException e) {
            e.getStackTrace();
        }


        return resultMap;
    }
}
