package com.best_store.right_bite.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/sec")
    public String sec() {
        return "authenticated";
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/anon")
    public String anon() {
        return "anonymous";
    }
}
