package com.spring.test.restapi.dto;

public class ToDoResponseDto {
    private int id;
    private String content;
    private boolean completed;

    public ToDoResponseDto(int id, String content, boolean completed) {
        this.id = id;
        this.content = content;
        this.completed = completed;
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public boolean isCompleted() {
        return completed;
    }
}
