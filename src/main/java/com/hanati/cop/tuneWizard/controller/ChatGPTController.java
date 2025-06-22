package com.hanati.cop.tuneWizard.controller;

import com.hanati.cop.tuneWizard.dto.ChatCompletionDTO;
import com.hanati.cop.tuneWizard.dto.CompletionRequestDTO;
import com.hanati.cop.tuneWizard.dto.RAGServerRequestDTO;
import com.hanati.cop.tuneWizard.service.CallHttpServiceImpl;
import com.hanati.cop.tuneWizard.service.ChatGPTService;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@CrossOrigin(origins = " ")
@RequestMapping(value = "/api/v1/chatGPT")
public class ChatGPTController {
    public final ChatGPTService chatGPTService;
    public final CallHttpServiceImpl callHttpService;
    public ChatGPTController(ChatGPTService chatGPTService, CallHttpServiceImpl callHttpService) {
        this.chatGPTService = chatGPTService;
        this.callHttpService = callHttpService;
    }

    @GetMapping("/modelList")
    public ResponseEntity<List<Map<String, Object>>> selectModelList() {
        List<Map<String, Object>> result = chatGPTService.modelList();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/model")
    public ResponseEntity<Map<String, Object>> isValidMOdel(@RequestParam(name = "modelName") String modelName) {
        Map<String, Object> result = chatGPTService.isValidModel(modelName);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/legacyPrompt")
    public ResponseEntity<Map<String, Object>> selectLegacyPrompt(@RequestBody CompletionRequestDTO completionRequestDTO) {
        log.debug("parma :: " + completionRequestDTO.toString());
        Map<String, Object> result = chatGPTService.legacyPrompt(completionRequestDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    @PostMapping("/prompt")
    public ResponseEntity<Map<String, Object>> selectPrompt(@RequestBody ChatCompletionDTO chatCompletionDTO) {
        log.debug("param :: " + chatCompletionDTO.toString());
        System.out.println(chatCompletionDTO.toString());
        Map<String, Object> result = chatGPTService.prompt(chatCompletionDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/flask")
    public ResponseEntity<Map<String, Object>> callflask(@RequestBody RAGServerRequestDTO ragServerRequestDTO) {
        log.debug("param :: " + ragServerRequestDTO.toString());
        System.out.println(ragServerRequestDTO.toString());
        Map<String, Object> result = callHttpService.CallFlaskLLM(ragServerRequestDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}

