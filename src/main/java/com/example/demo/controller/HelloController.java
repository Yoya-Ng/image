package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public String home() {
        return "OK - Spring Boot is running!";
    }

    @GetMapping("/hello")
    public String hello() {
        return "HELLO from Railway!";
    }

    @GetMapping("/health")
    public String health() {
        return "Healthy";
    }
}
