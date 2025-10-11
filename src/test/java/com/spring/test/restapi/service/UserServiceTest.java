package com.spring.test.restapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // import 추가

import com.spring.test.restapi.dto.UserRequestDto;
import com.spring.test.restapi.dto.UserResponseDto;
import com.spring.test.restapi.entity.User;
import com.spring.test.restapi.repository.UserRepository;

public class UserServiceTest {

    // 공통 테스트 데이터 및 유틸
    private final String username = "tester";
    private final String rawPassword = "12345678";
    private final UserRequestDto dto;
    private final User user;

    public UserServiceTest() {
        dto = new UserRequestDto();
        dto.setUsername(username);
        dto.setPassword(rawPassword);

        user = new User();
        user.setId(1);
        user.setUsername(username);
        user.setPassword(new BCryptPasswordEncoder().encode(rawPassword));
    }

    @Test
    void register_정상등록() {
        // given
        UserRepository userRepository = mock(UserRepository.class);
        UserService userService = new UserService(userRepository);

        // 이미 존재하는 사용자인지 체크
        when(userRepository.existsByUsername(username)).thenReturn(false);

        // 저장될 User 객체 준비
        when(userRepository.save(any(User.class))).thenReturn(user);

        // when
        UserResponseDto result = userService.register(dto);

        // then
        assertEquals(username, result.getUsername());
        verify(userRepository).existsByUsername(username);
        verify(userRepository).save(any(User.class));
    }

    // 이미 존재하는 아이디로 회원가입 시 예외 발생 테스트
    @Test
    void register_중복아이디_예외() {
        UserRepository userRepository = mock(UserRepository.class);
        UserService userService = new UserService(userRepository);

        when(userRepository.existsByUsername(username)).thenReturn(true);

        // then
        Exception ex = assertThrows(IllegalArgumentException.class, () -> userService.register(dto));
        assertEquals("이미 존재하는 아이디입니다.", ex.getMessage());
    }

    // 로그인 성공 테스트
    @Test
    void login_정상로그인() {
        UserRepository userRepository = mock(UserRepository.class);
        UserService userService = new UserService(userRepository);

        when(userRepository.findByUsername(username)).thenReturn(java.util.Optional.of(user));

        // when
        UserResponseDto result = userService.login(dto);

        // then
        assertEquals(username, result.getUsername());
        verify(userRepository).findByUsername(username);
    }

    // 로그인 실패(비밀번호 불일치/없는 아이디) 테스트
    @Test
    void login_없는아이디_예외() {
        UserRepository userRepository = mock(UserRepository.class);
        UserService userService = new UserService(userRepository);

        when(userRepository.findByUsername(username)).thenReturn(java.util.Optional.empty());

        // then
        Exception ex = assertThrows(IllegalArgumentException.class, () -> userService.login(dto));
        assertEquals("사용자를 찾을 수 없습니다.", ex.getMessage());
    }

    @Test
    void login_비밀번호불일치_예외() {
        UserRepository userRepository = mock(UserRepository.class);
        UserService userService = new UserService(userRepository);

        when(userRepository.findByUsername(username)).thenReturn(java.util.Optional.of(user));

        UserRequestDto wrongDto = new UserRequestDto();
        wrongDto.setUsername(username);
        wrongDto.setPassword("wrongpassword");

        // then
        Exception ex = assertThrows(IllegalArgumentException.class, () -> userService.login(wrongDto));
        assertEquals("비밀번호가 올바르지 않습니다.", ex.getMessage());
    }

}
