package com.example.taskplanner.controller;

import com.example.taskplanner.model.Folder;
import com.example.taskplanner.model.User;
import com.example.taskplanner.repository.FolderRepository;
import com.example.taskplanner.repository.UserRepository;
import com.example.taskplanner.service.FolderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import java.util.List;

@RestController
@RequestMapping("/api/folders")
public class FolderController {
    private final FolderService folderService;
    private final FolderRepository folderRepository;
    private final UserRepository userRepository;

    public FolderController(FolderService folderService, FolderRepository folderRepository, UserRepository userRepository) {
        this.folderService = folderService;
        this.folderRepository = folderRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Folder>> getUserFolders(@PathVariable Long userId) {
        List<Folder> folders = folderRepository.findByUserId(userId);
        return ResponseEntity.ok(folders);
    }

    @PostMapping("/add")
    public ResponseEntity<Folder> addFolder(@RequestBody Folder folder, @RequestParam Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        folder.setUser(user); // Устанавливаем пользователя
        Folder savedFolder = folderRepository.save(folder);
        return ResponseEntity.ok(savedFolder);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFolder(@PathVariable Long id) {
        folderService.deleteFolder(id);
        return ResponseEntity.noContent().build();
    }
}

