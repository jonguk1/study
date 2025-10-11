package com.spring.test.restapi.service;

import com.spring.test.restapi.dto.ToDoRequestDto;
import com.spring.test.restapi.dto.ToDoResponseDto;
import com.spring.test.restapi.entity.ToDo;
import com.spring.test.restapi.entity.User;
import com.spring.test.restapi.repository.ToDoRepository;
import com.spring.test.restapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ToDoServiceTest {

    private ToDoRepository toDoRepository;
    private UserRepository userRepository;
    private ToDoService toDoService;
    private String username;
    private User user;
    private ToDoRequestDto dto;

    @BeforeEach
    void setUp() {
        toDoRepository = mock(ToDoRepository.class);
        userRepository = mock(UserRepository.class);
        toDoService = new ToDoService(toDoRepository, userRepository);

        username = "tester";
        user = new User();
        user.setId(1);
        user.setUsername(username);

        dto = new ToDoRequestDto();
        dto.setContent("테스트 할 일");
        dto.setCompleted(false);
    }

    // 할 일 등록 정상 테스트
    @Test
    void addToDo_정상등록() {
        // given
        ToDo savedToDo = new ToDo();
        savedToDo.setId(1);
        savedToDo.setContent(dto.getContent());
        savedToDo.setCompleted(dto.isCompleted());
        savedToDo.setUser(user);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(toDoRepository.save(any(ToDo.class))).thenReturn(savedToDo);

        // when
        ToDoResponseDto result = toDoService.addToDo(dto, username);

        // then
        assertEquals(dto.getContent(), result.getContent());
        assertFalse(result.isCompleted());
        verify(userRepository).findByUsername(username);
        verify(toDoRepository).save(any(ToDo.class));
    }

    // 없는 사용자 예외 테스트
    @Test
    void addToDo_없는사용자_예외() {
        ToDoRepository toDoRepository = mock(ToDoRepository.class);
        UserRepository userRepository = mock(UserRepository.class);
        ToDoService toDoService = new ToDoService(toDoRepository, userRepository);

        String username = "notfound";
        ToDoRequestDto dto = new ToDoRequestDto();
        dto.setContent("테스트 할 일");
        dto.setCompleted(false);

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        Exception ex = assertThrows(IllegalArgumentException.class, () -> toDoService.addToDo(dto, username));
        assertEquals("사용자를 찾을 수 없습니다.", ex.getMessage());
    }

    // 할 일 전체 목록 조회 (본인 것만) 테스트
    @Test
    void getAllToDos_본인것만조회() {
        ToDoRepository toDoRepository = mock(ToDoRepository.class);
        UserRepository userRepository = mock(UserRepository.class);
        ToDoService toDoService = new ToDoService(toDoRepository, userRepository);

        String username = "tester";
        User user = new User();
        user.setId(1);
        user.setUsername(username);

        ToDo todo1 = new ToDo();
        todo1.setId(1);
        todo1.setContent("내 할 일");
        todo1.setCompleted(false);
        todo1.setUser(user);

        ToDo todo2 = new ToDo();
        todo2.setId(2);
        todo2.setContent("다른 사람 할 일");
        todo2.setCompleted(false);
        User otherUser = new User();
        otherUser.setId(2);
        otherUser.setUsername("other");
        todo2.setUser(otherUser);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(toDoRepository.findAll()).thenReturn(java.util.List.of(todo1, todo2));

        var result = toDoService.getAllToDos(username);
        assertEquals(1, result.size());
        assertEquals("내 할 일", result.get(0).getContent());
    }

    // 수정
    @Test
    void updateToDo_정상수정() {
        // given
        ToDo todo = new ToDo();
        todo.setId(1);
        todo.setContent("내 할 일");
        todo.setCompleted(false);
        todo.setUser(user);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(toDoRepository.findById(1)).thenReturn(Optional.of(todo));
        when(toDoRepository.save(any(ToDo.class))).thenReturn(todo);

        ToDoRequestDto updateDto = new ToDoRequestDto();
        updateDto.setContent("수정된 할 일");
        updateDto.setCompleted(true);

        // when
        ToDoResponseDto result = toDoService.updateToDo(1, updateDto, username);

        // then
        assertEquals(updateDto.getContent(), result.getContent());
        assertTrue(result.isCompleted());
    }

    // 삭제
    @Test
    void deleteToDo_정상삭제() {
        // given
        ToDo todo = new ToDo();
        todo.setId(1);
        todo.setContent("내 할 일");
        todo.setCompleted(false);
        todo.setUser(user);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(toDoRepository.findById(1)).thenReturn(Optional.of(todo));

        // when
        toDoService.deleteToDo(1, username);

        // then
        verify(toDoRepository).delete(todo);
    }

    // 권한 없는 경우 예외
    @Test
    void deleteToDo_권한없는경우_예외() {
        // given
        ToDo todo = new ToDo();
        todo.setId(1);
        todo.setContent("내 할 일");
        todo.setCompleted(false);
        User otherUser = new User();
        otherUser.setId(2);
        otherUser.setUsername("other");
        todo.setUser(otherUser);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(toDoRepository.findById(1)).thenReturn(Optional.of(todo));

        // when & then
        Exception ex = assertThrows(RuntimeException.class, () -> toDoService.deleteToDo(1, username));
        assertEquals("해당 할 일이 없거나 권한이 없습니다. id=1", ex.getMessage());
        verify(toDoRepository, never()).delete(any(ToDo.class));
    }
}
