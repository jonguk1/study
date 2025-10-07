package com.spring.test.restapi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ToDoPageController {

    @GetMapping("/todo")
    public String todoPage() {
        return "forward:/todo.html";
    }
}
