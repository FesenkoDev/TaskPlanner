package com.example.taskplanner.controller;

import com.example.taskplanner.model.Task;
import com.example.taskplanner.model.Folder;
import com.example.taskplanner.model.User;
import com.example.taskplanner.repository.FolderRepository;
import com.example.taskplanner.repository.TaskRepository;
import com.example.taskplanner.repository.UserRepository;
import com.example.taskplanner.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;
    private final FolderRepository folderRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskController(TaskService taskService, FolderRepository folderRepository, TaskRepository taskRepository, UserRepository userRepository) {
        this.taskService = taskService;
        this.folderRepository = folderRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<Task>> getUserTasks(@RequestParam Long userId) {
        List<Task> tasks = taskRepository.findByUserId(userId);
        return ResponseEntity.ok(tasks);
    }

    @PostMapping("/add")
    public ResponseEntity<Task> addTask(@RequestBody Task task) {
        if (task.getUser() == null || task.getUser().getId() == null) {
            throw new RuntimeException("userId обязателен");
        }
        if (task.getFolder() == null || task.getFolder().getId() == null) {
            throw new RuntimeException("folderId обязателен");
        }

        Folder folder = folderRepository.findById(task.getFolder().getId())
                .orElseThrow(() -> new RuntimeException("Папка не найдена"));
        User user = userRepository.findById(task.getUser().getId())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        task.setFolder(folder);
        task.setUser(user);

        Task savedTask = taskRepository.save(task);
        return ResponseEntity.ok(savedTask);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task taskDetails) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Задача не найдена"));

        task.setTitle(taskDetails.getTitle());
        task.setDescription(taskDetails.getDescription());
        task.setCompleted(taskDetails.isCompleted());

        Task updatedTask = taskRepository.save(task);
        return ResponseEntity.ok(updatedTask);
    }


    @PutMapping("/{id}/completed")
    public ResponseEntity<Task> updateTaskCompletion(
            @PathVariable Long id,
            @RequestBody Map<String, Boolean> update) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Задача не найдена"));

        task.setCompleted(update.get("completed"));
        taskRepository.save(task);

        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        if (!taskRepository.existsById(id)) {
            return ResponseEntity.badRequest().body("Задача не найдена");
        }

        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

}

