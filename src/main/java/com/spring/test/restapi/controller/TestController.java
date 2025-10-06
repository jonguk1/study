package com.spring.test.restapi.controller;

import com.spring.test.restapi.dto.Info;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.spring.test.restapi.service.TestService;

@RestController
@RequestMapping("/test")
public class TestController {
    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping("/get")
    ResponseEntity<?> getHelloWorld(@RequestParam(required = false) String name) {
        return new ResponseEntity<>(testService.myName(name), HttpStatus.OK);
    }

    @PostMapping("/post/{age}")
    ResponseEntity<?> postHelloWorld(@PathVariable("age") Integer age) {
        return new ResponseEntity<>(testService.myAge(age), HttpStatus.OK);
    }

    @PutMapping("/put")
    ResponseEntity<?> putHelloWorld(@RequestBody Info info) {
        return new ResponseEntity<>(testService.myInfo(info), HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    ResponseEntity<?> deleteHelloWorld(@RequestHeader String role, @RequestBody Info info) {
        return new ResponseEntity<>(testService.myInfoWithRole(role, info), HttpStatus.OK);
    }
}
