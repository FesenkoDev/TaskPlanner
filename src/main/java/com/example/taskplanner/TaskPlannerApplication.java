package com.example.taskplanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.example.taskplanner")
@EnableJpaRepositories
public class TaskPlannerApplication {
    public static void main(String[] args) {
        SpringApplication.run(TaskPlannerApplication.class, args);
    }
}
