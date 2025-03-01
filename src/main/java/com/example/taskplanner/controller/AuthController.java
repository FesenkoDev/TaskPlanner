package com.example.taskplanner.controller;

import com.example.taskplanner.service.AuthService;
import com.example.taskplanner.dto.RegisterRequest;
import com.example.taskplanner.model.User;
import com.example.taskplanner.dto.LoginRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) {
        try {
            User user = authService.registerUser(request.getUsername(), request.getEmail(), request.getPassword());
            return ResponseEntity.ok(Map.of("userId", user.getId())); // ✅ Теперь возвращаем userId
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage())); // ✅ Ошибки тоже в JSON
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            Long userId = authService.login(request.getUsername(), request.getPassword());
            return ResponseEntity.ok(Map.of("userId", userId)); // ✅ Теперь возвращаем userId в JSON
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid username or password")); // ✅ Ошибки тоже в JSON
        }
    }

}
