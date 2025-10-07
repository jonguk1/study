package com.spring.test.restapi.service;

import com.spring.test.restapi.dto.ToDoRequestDto;
import com.spring.test.restapi.dto.ToDoResponseDto;
import com.spring.test.restapi.entity.ToDo;
import com.spring.test.restapi.exception.NotFoundException;
import com.spring.test.restapi.repository.ToDoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ToDoService {

    private final ToDoRepository toDoRepository;

    public ToDoService(ToDoRepository toDoRepository) {
        this.toDoRepository = toDoRepository;
    }

    // 할 일 등록
    public ToDoResponseDto addToDo(ToDoRequestDto dto) {
        ToDo toDo = new ToDo();
        toDo.setContent(dto.getContent());
        toDo.setCompleted(dto.isCompleted());
        ToDo saved = toDoRepository.save(toDo);
        return toResponseDto(saved);
    }

    // 할 일 전체 목록 조회
    public List<ToDoResponseDto> getAllToDos() {
        return toDoRepository.findAll().stream()
                .map(this::toResponseDto)
                .toList();
    }

    // 할 일 단건 조회
    public ToDoResponseDto getToDoById(int id) {
        ToDo todo = toDoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("해당 할 일이 없습니다. id=" + id));
        return toResponseDto(todo);
    }

    // 할 일 수정
    public ToDoResponseDto updateToDo(int id, ToDoRequestDto dto) {
        ToDo existing = toDoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("해당 할 일이 없습니다. id=" + id));
        existing.setContent(dto.getContent());
        existing.setCompleted(dto.isCompleted());
        ToDo updated = toDoRepository.save(existing);
        return toResponseDto(updated);
    }

    // 할 일 삭제
    public void deleteToDo(int id) {
        if (!toDoRepository.existsById(id)) {
            throw new NotFoundException("해당 할 일이 없습니다. id=" + id);
        }
        toDoRepository.deleteById(id);
    }

    // 할 일 완료/미완료 상태 변경
    public void toggleCompleted(int id) {
        ToDo existing = toDoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("해당 할 일이 없습니다. id=" + id));
        existing.setCompleted(!existing.isCompleted());
        toDoRepository.save(existing);
    }

    // 엔티티 → 응답 DTO 변환 메서드
    private ToDoResponseDto toResponseDto(ToDo todo) {
        return new ToDoResponseDto(todo.getId(), todo.getContent(), todo.isCompleted());
    }
}
