package com.example.demoname.controller;

import com.example.demoname.domain.User;
import com.example.demoname.service.JwtService;
import com.example.demoname.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.ValidationException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private UserService userService;

    private JwtService jwtService;

    public UserController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @GetMapping("/auth")
    public Map auth(@RequestParam String login, @RequestParam String password) {
        User user = userService.findByLoginAndPassword(login, password).orElseThrow(() -> new ValidationException("Wrong name or password"));
        Map<String, String> res = new HashMap<>();
        res.put("token", jwtService.create(user));
        return res;
    }

    @PostMapping
    public ResponseEntity<?> register(@RequestParam String login, @RequestParam String password) {
        User user = new User(login);
        userService.save(user, password);
        return ResponseEntity.ok().build();
    }
}
