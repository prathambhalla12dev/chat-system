package com.chat.service;

import com.chat.dto.ApiResponse;
import com.chat.dto.SendMessageRequest;
import com.chat.model.Message;
import com.chat.repository.MessageRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public ApiResponse<Message> sendMessage(SendMessageRequest request) {

        Message message = new Message(
                request.getSenderId(),
                request.getReceiverId(),
                request.getContent()
        );

        Message savedMessage = messageRepository.save(message);

        return new ApiResponse<>(
                true,
                "Message sent successfully",
                HttpStatus.CREATED,
                savedMessage
        );
    }

    public ApiResponse<List<Message>> getConversation(
            String userId1,
            String userId2
    ) {

        List<Message> messages = messageRepository
                .findBySenderIdAndReceiverIdOrSenderIdAndReceiverIdOrderByTimestampAsc(
                        userId1,
                        userId2,
                        userId2,
                        userId1
                );

        return new ApiResponse<>(
                true,
                "Conversation fetched successfully",
                HttpStatus.OK,
                messages
        );
    }
}
