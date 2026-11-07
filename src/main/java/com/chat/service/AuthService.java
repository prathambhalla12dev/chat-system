
package com.chat.service;

import com.chat.dto.ApiResponse;
import com.chat.dto.LoginRequest;
import com.chat.dto.RegisterRequest;
import com.chat.dto.UserResponse;
import com.chat.model.User;
import com.chat.repository.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ApiResponse<UserResponse> register(RegisterRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            return new ApiResponse<>(
                    false,
                    "Username already exists",
                    HttpStatus.CONFLICT,
                    null
            );
        }

        String hashedPassword =
                passwordEncoder.encode(request.getPassword());

        User user = new User(
                request.getUsername(),
                hashedPassword
        );

        User savedUser = userRepository.save(user);

        return new ApiResponse<>(
                true,
                "User registered successfully",
                HttpStatus.CREATED,
                UserResponse.from(savedUser)
        );
    }

    public ApiResponse<UserResponse> login(LoginRequest request) {

        Optional<User> optionalUser =
                userRepository.findByUsername(request.getUsername());

        if (optionalUser.isEmpty()) {
            return new ApiResponse<>(
                    false,
                    "Invalid username or password",
                    HttpStatus.UNAUTHORIZED,
                    null
            );
        }

        User user = optionalUser.get();

        boolean authenticated = passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        );

        if (!authenticated) {
            return new ApiResponse<>(
                    false,
                    "Invalid username or password",
                    HttpStatus.UNAUTHORIZED,
                    null
            );
        }

        return new ApiResponse<>(
                true,
                "Login successful",
                HttpStatus.OK,
                UserResponse.from(user)
        );
    }
}
