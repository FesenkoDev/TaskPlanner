package com.example.taskplanner.service;

import com.example.taskplanner.repository.UserRepository;
import com.example.taskplanner.model.User;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(String username, String email, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already exists!");
        }
        if(userRepository.findByEmail(email.toLowerCase()).isPresent()) { // ✅ Приводим email к нижнему регистру
            throw new RuntimeException("Email already exists!");
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email.toLowerCase()); // ✅ Сохраняем email в нижнем регистре
        user.setPassword(passwordEncoder.encode(password));

        return userRepository.save(user);
    }

    public Long login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Incorrect password");
        }

        return user.getId(); // ✅ Возвращаем userId
    }

}
