package com.spring.test.restapi.repository;

import com.spring.test.restapi.entity.ToDo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ToDoRepository extends JpaRepository<ToDo, Integer> {
    // 필요시 사용자별 할 일 조회 등 커스텀 메서드 추가 가능
}
