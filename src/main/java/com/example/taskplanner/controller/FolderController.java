package com.example.taskplanner.controller;

import com.example.taskplanner.model.Folder;
import com.example.taskplanner.service.FolderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;

import com.example.taskplanner.model.User;
import com.example.taskplanner.repository.UserRepository;
import com.example.taskplanner.repository.FolderRepository;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/folders")
public class FolderController {
    private final FolderService folderService;
    private final UserRepository userRepository;
    private final FolderRepository folderRepository;

    public FolderController(FolderService folderService, UserRepository userRepository, FolderRepository folderRepository) {
        this.folderService = folderService;
        this.userRepository = userRepository;
        this.folderRepository = folderRepository;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Folder>> getAllFolders() {
        return ResponseEntity.ok(folderService.getAllFolders());
    }

    @PostMapping("/add")
    public ResponseEntity<Folder> createFolder(@RequestBody Map<String, Object> payload) {
        // 🔥 Получаем данные из JSON
        String name = (String) payload.get("name");
        String emoji = (String) payload.getOrDefault("emoji", "📁"); // Эмодзи по умолчанию
        Long userId = ((Number) payload.get("userId")).longValue();

        // 🔥 Загружаем пользователя
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден!"));

        // 🔥 Создаем и сохраняем папку
        Folder newFolder = new Folder(name, emoji, user);
        folderRepository.save(newFolder);

        return ResponseEntity.ok(newFolder);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFolder(@PathVariable Long id) {
        folderService.deleteFolder(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user")
    public ResponseEntity<List<Folder>> getFoldersByUser(@RequestParam Long userId) {
        return ResponseEntity.ok(folderService.getFoldersByUserId(userId));
    }

}
