package com.hanati.cop.tuneWizard.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanati.cop.tuneWizard.dto.ChatCompletionDTO;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/PythonTest")
public class CallPythonFuncController {
    private static final Logger log = LoggerFactory.getLogger(CallPythonFuncController.class);

    @PostMapping("/prompt")
    public ResponseEntity<Map<String, Object>> selectPrompt(@RequestBody ChatCompletionDTO chatCompletionDTO) throws JsonProcessingException {
        log.debug("param :: " + chatCompletionDTO.toString());
        System.out.println(chatCompletionDTO.toString());

        Map<String, Object> params = new HashMap<>();
        params.put("name", "홍길동");
        params.put("age", 30);
        params.put("city", "서울");
        params.put("job", "개발자");

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(params);

        String pythonScriptPath = new File("src/main/java/com/hanati/cop/tuneWizard/pythonCode/helloPython.py").getAbsolutePath();
        // hello.py 소스코드를 실행하는 python 프로세스 생성
        ProcessBuilder pb = new ProcessBuilder("python", pythonScriptPath, jsonString);

        // 프로세스 실행
        Process process = null;
        int exitCode = 0 ;
        StringBuilder output = new StringBuilder();
        try {
            process = pb.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line);
            }

            exitCode = process.waitFor();
            System.out.println("Python exited with code: " + exitCode);


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

// 결과 파싱
        String pythonOutput = output.toString();
        System.out.println("Python Output JSON: " + pythonOutput);

        // 다시 자바 Map으로 역직렬화
        Map<String, Object> resultMap = mapper.readValue(pythonOutput, new TypeReference<Map<String, Object>>() {});
        System.out.println("메시지: " + resultMap.get("message"));
        
        
        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }

}
