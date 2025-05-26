package com.finalYearProject.product.controller;


import com.finalYearProject.product.service.GeminiChatBotApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("api/chat")
@RequiredArgsConstructor
public class GeminiChatBotController {


    private final GeminiChatBotApiService apiService;

    @PostMapping("send-message")
    public ResponseEntity<Map<String,String>> sendMessage(@RequestBody String message) {

        return new ResponseEntity<>(apiService.sendMessageToGemini(message), HttpStatus.OK);
    }

}
