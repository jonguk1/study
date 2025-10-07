package com.spring.test.restapi.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import com.spring.test.restapi.entity.ToDo;
import com.spring.test.restapi.service.ToDoService;

@RestController
@RequestMapping("/api/todos")
public class ToDoController {

    private final ToDoService toDoService;

    public ToDoController(ToDoService toDoService) {
        this.toDoService = toDoService;
    }

    // 할 일 등록
    @PostMapping
    public ToDo addToDo(@RequestBody ToDo toDo) {
        return toDoService.addToDo(toDo);
    }

    // 할 일 전체 목록 조회
    @GetMapping
    public List<ToDo> getAllToDos() {
        return toDoService.getAllToDos();
    }

    // 할 일 단건 조회
    @GetMapping("/{id}")
    public ToDo getToDoById(@PathVariable("id") int id) {
        return toDoService.getToDoById(id);
    }

    // 할 일 수정
    @PutMapping("/{id}")
    public ToDo updateToDo(@PathVariable("id") int id, @RequestBody ToDo toDo) {
        return toDoService.updateToDo(id, toDo);
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
