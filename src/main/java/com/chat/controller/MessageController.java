
package com.chat.controller;

import com.chat.dto.ApiResponse;
import com.chat.dto.SendMessageRequest;
import com.chat.model.Message;
import com.chat.service.MessageService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Message>> sendMessage(
            @RequestBody SendMessageRequest request
    ) {
        ApiResponse<Message> response =
                messageService.sendMessage(request);

        return ResponseEntity
                .status(response.getHttpCode())
                .body(response);
    }

    @GetMapping("/conversation/{userId1}/{userId2}")
    public ResponseEntity<ApiResponse<List<Message>>> getConversation(
            @PathVariable String userId1,
            @PathVariable String userId2
    ) {
        ApiResponse<List<Message>> response =
                messageService.getConversation(userId1, userId2);

        return ResponseEntity
                .status(response.getHttpCode())
                .body(response);
    }
}
