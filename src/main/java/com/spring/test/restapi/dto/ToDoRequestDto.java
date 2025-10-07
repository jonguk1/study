package com.spring.test.restapi.dto;

import jakarta.validation.constraints.NotBlank;

public class ToDoRequestDto {
    @NotBlank(message = "할 일 내용은 필수입니다.")
    private String content;

    private boolean completed;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
