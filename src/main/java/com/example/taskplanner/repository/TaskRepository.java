package com.example.taskplanner.repository;

import com.example.taskplanner.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByFolderId(Long folderId); // ✅ Этот метод должен быть!
    List<Task> findByUserId(Long userId); // Получаем только задачи этого пользователя
}
