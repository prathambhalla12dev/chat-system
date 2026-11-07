package com.chat.dto;

import com.chat.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private String id;
    private String username;

    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getUsername());
    }

}
