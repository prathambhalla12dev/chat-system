package com.chat.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Document(collection = "messages")
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    @Id
    private String id;

    private String senderId;

    private String receiverId;

    private String content;

    private long timestamp;

    public Message(String senderId, String receiverId, String content) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.timestamp = System.currentTimeMillis();
    }
}
