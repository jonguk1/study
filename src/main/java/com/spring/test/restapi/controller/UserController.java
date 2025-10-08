package com.spring.test.restapi.controller;

import com.spring.test.restapi.dto.UserRequestDto;
import com.spring.test.restapi.dto.UserResponseDto;
import com.spring.test.restapi.service.UserService;
import com.spring.test.restapi.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    // 회원가입
    @PostMapping("/register")
    public UserResponseDto register(@RequestBody @Valid UserRequestDto dto) {
        return userService.register(dto);
    }

    // 로그인 (JWT 토큰 발급)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserRequestDto dto) {
        UserResponseDto user = userService.login(dto);
        String accessToken = jwtUtil.generateAccessToken(user.getUsername());
        String refreshToken = jwtUtil.generateRefreshToken(user.getUsername());
        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + accessToken)
                .header("Refresh-Token", refreshToken)
                .body(user);
    }

    // 로그아웃 (클라이언트에서 토큰 삭제 안내)
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // JWT는 서버에서 만료 불가, 클라이언트에서 토큰 삭제(무효화) 필요
        return ResponseEntity.ok().body("로그아웃 되었습니다. 클라이언트에서 토큰을 삭제하세요.");
    }

    // RefreshToken으로 AccessToken 재발급
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestHeader("Refresh-Token") String refreshToken) {
        if (!jwtUtil.validateToken(refreshToken)) {
            return ResponseEntity.status(401).body("RefreshToken이 유효하지 않습니다.");
        }
        String username = jwtUtil.getUsernameFromToken(refreshToken);
        String newAccessToken = jwtUtil.generateAccessToken(username);
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + newAccessToken)
                .body("AccessToken이 재발급되었습니다.");
    }
}
