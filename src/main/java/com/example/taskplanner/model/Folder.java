package com.example.taskplanner.model;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "folders")
@JsonIgnoreProperties({"user"}) // ✅ Убираем зацикливание
public class Folder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(columnDefinition = "TEXT") // Позволяет хранить эмодзи
    private String emoji = "📁"; // Значение по умолчанию

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Folder() {}

    public Folder(String name, String emoji, User user) {
        this.name = name;
        this.emoji = emoji;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    public User getUser() {return user;}
    public void setUser(User user) {this.user = user;}
}
