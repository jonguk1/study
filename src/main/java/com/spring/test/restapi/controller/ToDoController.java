package com.spring.test.restapi.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.spring.test.restapi.dto.ToDoRequestDto;
import com.spring.test.restapi.dto.ToDoResponseDto;
import com.spring.test.restapi.service.ToDoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/todos")
public class ToDoController {

    private final ToDoService toDoService;

    public ToDoController(ToDoService toDoService) {
        this.toDoService = toDoService;
    }

    // 할 일 등록
    @PostMapping
    public ToDoResponseDto addToDo(@RequestBody @Valid ToDoRequestDto dto) {
        return toDoService.addToDo(dto);
    }

    // 할 일 전체 목록 조회
    @GetMapping
    public List<ToDoResponseDto> getAllToDos() {
        return toDoService.getAllToDos();
    }

    // 할 일 단건 조회
    @GetMapping("/{id}")
    public ToDoResponseDto getToDoById(@PathVariable("id") int id) {
        return toDoService.getToDoById(id);
    }

    // 할 일 수정
    @PutMapping("/{id}")
    public ToDoResponseDto updateToDo(@PathVariable("id") int id, @RequestBody @Valid ToDoRequestDto dto) {
        return toDoService.updateToDo(id, dto);
    }

    // 할 일 삭제
    @DeleteMapping("/{id}")
    public void deleteToDo(@PathVariable("id") int id) {
        toDoService.deleteToDo(id);
    }

    // 할 일 완료/미완료 상태 변경
    @PutMapping("/{id}/toggle")
    public void toggleCompleted(@PathVariable("id") int id) {
        toDoService.toggleCompleted(id);
    }

}
