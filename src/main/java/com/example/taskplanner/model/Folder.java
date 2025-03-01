package com.example.taskplanner.model;

import jakarta.persistence.*;

@Entity
@Table(name = "folders")
public class Folder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(columnDefinition = "TEXT") // Позволяет хранить эмодзи
    private String emoji = "📁"; // Значение по умолчанию

    @ManyToOne
    @JoinColumn(name = "user_id")
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
