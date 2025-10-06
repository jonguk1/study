package com.spring.test.restapi.repository;

import com.spring.test.restapi.entity.ToDo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ToDoRepository extends JpaRepository<ToDo, Integer> {
    // 별도 구현 없이 JpaRepository가 기본 CRUD 제공
}
