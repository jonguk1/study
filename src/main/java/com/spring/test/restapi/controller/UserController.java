package com.spring.test.restapi.controller;

import com.spring.test.restapi.dto.ErrorResponseDto;
import com.spring.test.restapi.dto.UserRequestDto;
import com.spring.test.restapi.dto.UserResponseDto;
import com.spring.test.restapi.service.UserService;
import com.spring.test.restapi.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
// Swagger/OpenAPI import
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

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
    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원가입 성공", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "입력값 오류/중복 아이디", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PostMapping("/register")
    public UserResponseDto register(@RequestBody @Valid UserRequestDto dto) {
        return userService.register(dto);
    }

    // 로그인
    @Operation(summary = "로그인", description = "로그인 성공 시 JWT 토큰을 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "입력값 오류", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "아이디/비밀번호 불일치", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserRequestDto dto) {
        UserResponseDto user = userService.login(dto);
        String token = jwtUtil.generateAccessToken(user.getUsername());
        String refreshToken = jwtUtil.generateRefreshToken(user.getUsername());
        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + token)
                .header("Refresh-Token", refreshToken)
                .body(user);
    }

    // 로그아웃
    @Operation(summary = "로그아웃", description = "클라이언트에서 토큰을 삭제하도록 안내합니다.")
    @ApiResponse(responseCode = "200", description = "로그아웃 안내", content = @Content(schema = @Schema(implementation = String.class)))
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok().body("로그아웃 되었습니다. 클라이언트에서 토큰을 삭제하세요.");
    }

    // AccessToken 재발급
    @Operation(summary = "AccessToken 재발급", description = "RefreshToken으로 AccessToken을 재발급합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "재발급 성공", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "401", description = "RefreshToken이 유효하지 않음", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestHeader("Refresh-Token") String refreshToken) {
        if (!jwtUtil.validateToken(refreshToken)) {
            return ResponseEntity.status(401).body("RefreshToken이 유효하지 않습니다.");
        }
        String username = jwtUtil.getUsernameFromToken(refreshToken);
        String newAccessToken = jwtUtil.generateAccessToken(username);
        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + newAccessToken)
                .body("AccessToken이 재발급되었습니다.");
    }
}
