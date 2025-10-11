package com.spring.test.restapi.dto;

import java.time.LocalDateTime;

public class UserResponseDto {
    private int id;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UserResponseDto(int id, String username, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.username = username;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
