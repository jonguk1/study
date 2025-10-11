package com.spring.test.restapi.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.spring.test.restapi.dto.ToDoRequestDto;
import com.spring.test.restapi.dto.ToDoResponseDto;
import com.spring.test.restapi.service.ToDoService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;

// Swagger/OpenAPI import
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import com.spring.test.restapi.dto.ErrorResponseDto;

@RestController
@RequestMapping("/api/todos")
public class ToDoController {

    private final ToDoService toDoService;

    public ToDoController(ToDoService toDoService) {
        this.toDoService = toDoService;
    }

    // 할 일 등록
    @Operation(summary = "할 일 등록", description = "새로운 할 일을 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "등록 성공", content = @Content(schema = @Schema(implementation = ToDoResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PostMapping
    public ToDoResponseDto addToDo(
            @RequestBody @Valid ToDoRequestDto dto,
            Authentication authentication) {
        String username = authentication.getName();
        return toDoService.addToDo(dto, username);
    }

    // 할 일 전체 목록 조회
    @Operation(summary = "할 일 전체 목록 조회", description = "로그인한 사용자의 모든 할 일 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ToDoResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping
    public List<ToDoResponseDto> getAllToDos(Authentication authentication) {
        String username = authentication.getName();
        return toDoService.getAllToDos(username);
    }

    // 할 일 수정
    @Operation(summary = "할 일 수정", description = "할 일 내용을 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공", content = @Content(schema = @Schema(implementation = ToDoResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "할 일 없음/권한 없음", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PutMapping("/{id}")
    public ToDoResponseDto updateToDo(
            @Parameter(description = "할 일 ID") @PathVariable("id") int id,
            @RequestBody @Valid ToDoRequestDto dto,
            Authentication authentication) {
        String username = authentication.getName();
        return toDoService.updateToDo(id, dto, username);
    }

    // 할 일 삭제
    @Operation(summary = "할 일 삭제", description = "할 일을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "삭제 성공", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "할 일 없음/권한 없음", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @DeleteMapping("/{id}")
    public void deleteToDo(
            @Parameter(description = "할 일 ID") @PathVariable("id") int id,
            Authentication authentication) {
        String username = authentication.getName();
        toDoService.deleteToDo(id, username);
    }

    // 할 일 완료/미완료 토글
    @Operation(summary = "할 일 완료/미완료 토글", description = "할 일의 완료 상태를 토글합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "토글 성공", content = @Content(schema = @Schema(implementation = ToDoResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "할 일 없음/권한 없음", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PutMapping("/{id}/toggle")
    public void toggleCompleted(
            @Parameter(description = "할 일 ID") @PathVariable("id") int id,
            Authentication authentication) {
        String username = authentication.getName();
        toDoService.toggleCompleted(id, username);
    }
}
