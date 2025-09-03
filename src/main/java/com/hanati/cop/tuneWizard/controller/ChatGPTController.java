package com.hanati.cop.tuneWizard.controller;

import com.hanati.cop.tuneWizard.dto.RAGServerRequestDTO;
import com.hanati.cop.tuneWizard.dto.TableInfoListRequestDTO;
import com.hanati.cop.tuneWizard.service.CallHttpServiceImpl;
import com.hanati.cop.tuneWizard.legacy.service.ChatGPTService;
import com.hanati.cop.tuneWizard.service.DBDataServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@CrossOrigin(origins = " ")
@RequestMapping(value = "/api/v1/chatGPT")
public class ChatGPTController {
    public final CallHttpServiceImpl callHttpService;
    public final DBDataServiceImpl dbDataServiceImpl;
    public ChatGPTController(ChatGPTService chatGPTService
            , CallHttpServiceImpl callHttpService
            , DBDataServiceImpl dbDataServiceImpl) {
        this.callHttpService = callHttpService;
        this.dbDataServiceImpl = dbDataServiceImpl;
    }

    @PostMapping("/callPrompt")
    public ResponseEntity<Map<String, Object>> callPrompt(@RequestBody RAGServerRequestDTO ragServerRequestDTO) {
        log.debug("param :: " + ragServerRequestDTO.toString());
        System.out.println(ragServerRequestDTO.toString());
        Map<String, Object> result = callHttpService.CallFlaskLLM(ragServerRequestDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/tableList")
    public ResponseEntity<List<String>> tableList() {
        List<String> result = dbDataServiceImpl.tableList();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/tableInfoList")
    public ResponseEntity<HashMap<String, ArrayList<String>>> tableInfoList(@RequestBody TableInfoListRequestDTO tableInfoListRequestDTO){
        HashMap<String, ArrayList<String>> result = dbDataServiceImpl.makePromptTableInfo(tableInfoListRequestDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
