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

    @GetMapping("/by-folder")
    public ResponseEntity<List<Task>> getTasksByFolder(@RequestParam Long folderId) {

        List<Task> tasks = taskRepository.findByFolderId(folderId); // Фильтруем задачи по папке

        return ResponseEntity.ok(tasks);
    }

    @PostMapping("/add")
    public ResponseEntity<Task> createTask(@RequestBody Map<String, Object> payload) {
        String title = (String) payload.get("title");
        String description = (String) payload.get("description");
        Boolean completed = (Boolean) payload.get("completed");
        Long folderId = ((Number) payload.get("folderId")).longValue();

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
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
