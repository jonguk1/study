package com.spring.test.restapi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ToDoPageController {

    // /todo로 접속 시 todo.html로 forward
    @GetMapping("/todo")
    public String todoPage() {
        return "forward:/todo.html";
    }

    // index.html은 별도 매핑 없이 /로 접속하면 자동으로 열림
    @GetMapping("/register")
    public String registerPage() {
        return "forward:/register.html";
    }
}
