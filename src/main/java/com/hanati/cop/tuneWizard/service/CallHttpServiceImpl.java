package com.hanati.cop.tuneWizard.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanati.cop.tuneWizard.config.ChatGPTConfig;
import com.hanati.cop.tuneWizard.dao.ChatTableIndexListDAO;
import com.hanati.cop.tuneWizard.dao.PromptInsertDAO;
import com.hanati.cop.tuneWizard.dao.TableCountDAO;
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
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.SimpleFormatter;


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

        Map<String, Object> resultMap = new HashMap<>();
        String table = rag.getTable();
        String query = rag.getQuery();
        //1. 화면에서 넘어온 프롬포트에 인덱스 데이터와 해당 테이블의 총 건수를 조회한 결과를 추가함
        //인덱스 데이터 조회하여 세팅하기
        List<ChatTableIndexListDAO> list = mapperClass.tableIndexList(table);
        TableCountDAO tableCountDAO = mapperClass.tableCount(table);
        String startStringPrompt = "인덱스 구성은 다음과 같습니다. \n";

        for(int i = 0; i<list.size(); i++) {
            String indexName = list.get(i).getINDEX_NAME();
            String columnName = list.get(i).getCOLUMN_NAME();

            String totalStringPrompt = "인덱스의 이름 : " + indexName + " 인덱스의 구성 컬럼 " + columnName + "\n";
            System.out.println(totalStringPrompt);
            startStringPrompt += totalStringPrompt;
        }
        String countPrompt = "현재 테이블의 데이터 양은 " + tableCountDAO.getCNT() + "개 입니다";
        startStringPrompt += countPrompt;
        query += startStringPrompt;
        System.out.println(query + startStringPrompt);

        if("---".equals(table) || "".equals(table)){
            rag.setIndexList("");
        }else {
            rag.setIndexList(startStringPrompt);
        }

        //2. 프롬포트를 포함한 데이터로 RAG를 포함하는 FLASK 서버로 요청
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
        String answer_data = resultMap.get("answer").toString();

        //3. 최종 요청 프롬포트와 응답 프롬포트 데이터를 신규 테이블에 입력
        PromptInsertDAO promptInsertDAO = new PromptInsertDAO();
        promptInsertDAO.setUuid(UUID.randomUUID().toString());
        promptInsertDAO.setPrompt_text(query);
        promptInsertDAO.setResponse_text(answer_data);
        promptInsertDAO.setCall_date(getTodayDate());
        promptInsertDAO.setCall_time(getNowTime());
        try{
            int promptinsertResult = 0;
            promptinsertResult = mapperClass.insertPrompt(promptInsertDAO);
            if(promptinsertResult == 0){
                throw new SQLException("정상적으로 삽입되지 않았습니다.");
            }
        }catch (SQLException e ){
            e.getStackTrace();
        }

        return resultMap;
    }
    String getTodayDate() {
        Date nowDate = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String today = "";
        today = simpleDateFormat.format(nowDate);

        return today;
    }
    String getNowTime() {
        Date now = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hhmmss");
        String nowTime = "";
        nowTime = simpleDateFormat.format(now);

        return nowTime;
    }
}
