package com.spring.test.restapi.service;

import com.spring.test.restapi.dto.ToDoRequestDto;
import com.spring.test.restapi.dto.ToDoResponseDto;
import com.spring.test.restapi.entity.ToDo;
import com.spring.test.restapi.entity.User;
import com.spring.test.restapi.exception.NotFoundException;
import com.spring.test.restapi.repository.ToDoRepository;
import com.spring.test.restapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ToDoService {

    private final ToDoRepository toDoRepository;
    private final UserRepository userRepository;

    public ToDoService(ToDoRepository toDoRepository, UserRepository userRepository) {
        this.toDoRepository = toDoRepository;
        this.userRepository = userRepository;
    }

    // 공통 사용자 조회 메서드
    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }

    // 할 일 등록 (User 연관관계)
    public ToDoResponseDto addToDo(ToDoRequestDto dto, String username) {
        User user = getUserByUsername(username);
        ToDo toDo = new ToDo();
        toDo.setContent(dto.getContent());
        toDo.setCompleted(dto.isCompleted());
        toDo.setUser(user); // 연관관계 설정
        ToDo saved = toDoRepository.save(toDo);
        return toResponseDto(saved);
    }

    // 할 일 전체 목록 조회
    public List<ToDoResponseDto> getAllToDos(String username) {
        User user = getUserByUsername(username);
        return toDoRepository.findAll().stream()
                .filter(todo -> todo.getUser() != null &&
                        todo.getUser().getId() == user.getId()) // 사용자별 필터링
                .map(this::toResponseDto)
                .toList();
    }

    // 할 일 단건 조회
    public ToDoResponseDto getToDoById(int id, String username) {
        User user = getUserByUsername(username);
        ToDo todo = toDoRepository.findById(id)
                .filter(t -> t.getUser() != null &&
                        t.getUser().getId() == user.getId())
                .orElseThrow(() -> new NotFoundException("해당 할 일이 없거나 권한이 없습니다. id=" + id));
        return toResponseDto(todo);
    }

    // 할 일 수정
    public ToDoResponseDto updateToDo(int id, ToDoRequestDto dto, String username) {
        User user = getUserByUsername(username);
        ToDo existing = toDoRepository.findById(id)
                .filter(t -> t.getUser() != null &&
                        t.getUser().getId() == user.getId())
                .orElseThrow(() -> new NotFoundException("해당 할 일이 없거나 권한이 없습니다. id=" + id));
        existing.setContent(dto.getContent());
        existing.setCompleted(dto.isCompleted());
        existing.setUser(user);
        ToDo updated = toDoRepository.save(existing);
        return toResponseDto(updated);
    }

    // 할 일 삭제 (본인 것만)
    public void deleteToDo(int id, String username) {
        User user = getUserByUsername(username);
        ToDo todo = toDoRepository.findById(id)
                .filter(t -> t.getUser() != null && t.getUser().getId() == user.getId())
                .orElseThrow(() -> new NotFoundException("해당 할 일이 없거나 권한이 없습니다. id=" + id));
        toDoRepository.delete(todo);
    }

    // 할 일 완료/미완료 상태 변경 (본인 것만)
    public void toggleCompleted(int id, String username) {
        User user = getUserByUsername(username);
        ToDo existing = toDoRepository.findById(id)
                .filter(t -> t.getUser() != null && t.getUser().getId() == user.getId())
                .orElseThrow(() -> new NotFoundException("해당 할 일이 없거나 권한이 없습니다. id=" + id));
        existing.setCompleted(!existing.isCompleted());
        toDoRepository.save(existing);
    }

    // 엔티티 → 응답 DTO 변환 메서드
    private ToDoResponseDto toResponseDto(ToDo todo) {
        return new ToDoResponseDto(todo.getId(), todo.getContent(), todo.isCompleted());
    }
}
