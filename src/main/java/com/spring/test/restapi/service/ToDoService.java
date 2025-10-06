package com.spring.test.restapi.service;

import org.springframework.stereotype.Service;

import com.spring.test.restapi.entity.ToDo;
import com.spring.test.restapi.repository.ToDoRepository;

import java.util.List;

@Service
public class ToDoService {

    private final ToDoRepository toDoRepository;

    public ToDoService(ToDoRepository toDoRepository) {
        this.toDoRepository = toDoRepository;
    }

    // 할 일 등록
    public ToDo addToDo(ToDo toDo) {
        return toDoRepository.save(toDo);
    }

    // 할 일 전체 목록 조회
    public List<ToDo> getAllToDos() {
        return toDoRepository.findAll();
    }

    // 할 일 단건 조회
    public ToDo getToDoById(int id) {
        return toDoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 할 일이 없습니다. id=" + id));
    }

    // 할 일 수정
    public ToDo updateToDo(int id, ToDo toDo) {
        ToDo existing = toDoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 할 일이 없습니다. id=" + id));
        existing.setContent(toDo.getContent());
        existing.setCompleted(toDo.isCompleted());
        return toDoRepository.save(existing);
    }

    // 할 일 삭제
    public void deleteToDo(int id) {
        if (!toDoRepository.existsById(id)) {
            throw new IllegalArgumentException("해당 할 일이 없습니다. id=" + id);
        }
        toDoRepository.deleteById(id);
    }

    // 할 일 완료/미완료 상태 변경
    public void toggleCompleted(int id) {
        ToDo existing = toDoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 할 일이 없습니다. id=" + id));
        existing.setCompleted(!existing.isCompleted());
        toDoRepository.save(existing);
    }
}
