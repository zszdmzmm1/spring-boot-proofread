package com.auefly.spring.boot.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PageController {
    @GetMapping("/")
    public String index() {
        return "Hello world.";
    }
}
