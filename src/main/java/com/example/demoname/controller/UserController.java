package com.example.demoname.controller;

import com.example.demoname.domain.User;
import com.example.demoname.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost", maxAge = 3600)
@RestController
@RequestMapping("/api/users")
public class UserController {
    UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public User auth(@RequestParam String login, @RequestParam String password) {
        return null;
    }

}
