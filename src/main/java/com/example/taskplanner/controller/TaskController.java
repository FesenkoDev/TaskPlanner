package com.example.taskplanner.controller;

import com.example.taskplanner.model.Task;
import com.example.taskplanner.model.Folder;
import com.example.taskplanner.repository.FolderRepository;
import com.example.taskplanner.repository.TaskRepository;
import com.example.taskplanner.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Map;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;
    private final FolderRepository folderRepository;
    private final TaskRepository taskRepository;


    public TaskController(TaskService taskService, FolderRepository folderRepository, TaskRepository taskRepository) {
        this.taskService = taskService;
        this.folderRepository = folderRepository;
        this.taskRepository = taskRepository;
    }

    @GetMapping
    public List<Task> getAllTask() {
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        Optional<Task> task = taskService.getTaskById(id);
        return task.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<Task>> getTasksByFolder(@RequestParam Long folderId) {
        System.out.println("📡 Получаем задачи для folderId: " + folderId); // Логируем
        List<Task> tasks = taskRepository.findByFolderId(folderId); // Фильтруем задачи по папке
        System.out.println("✅ Найдено задач: " + tasks.size()); // Логируем

        return ResponseEntity.ok(tasks);
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Map<String, Object> payload) {
        System.out.println("Получен payload: " + payload); // ✅ Логируем входные данные

        String title = (String) payload.get("title");
        String description = (String) payload.get("description");
        Boolean completed = (Boolean) payload.get("completed");
        Long folderId = ((Number) payload.get("folderId")).longValue();

        System.out.println("Создаем задачу с title: " + title + ", folderId: " + folderId); // ✅ Проверяем

        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new RuntimeException("Папка не найдена"));

        Task newTask = new Task(title, description, completed, folder);
        taskRepository.save(newTask);

        return ResponseEntity.ok(newTask);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task taskDetails) {
        try {
            Task updatedTask = taskService.updateTask(id, taskDetails);
            return ResponseEntity.ok(updatedTask);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
