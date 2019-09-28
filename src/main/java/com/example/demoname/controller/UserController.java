package com.example.demoname.controller;

import com.example.demoname.domain.User;
import com.example.demoname.dto.UserDTO;
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
@CrossOrigin
@RequestMapping("/api/users")
public class UserController {
    private UserService userService;

    private JwtService jwtService;

    public UserController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @GetMapping("/auth")
    public Map auth(@RequestBody UserDTO userDTO) {
        User user = userService.findByLoginAndPassword(userDTO.getLogin(), userDTO.getPassword()).orElseThrow(() -> new ValidationException("Wrong name or password"));
        Map<String, String> res = new HashMap<>();
        res.put("token", jwtService.create(user));
        return res;
    }

    @PostMapping
    public ResponseEntity<?> register(@RequestBody UserDTO userDTO) {
        User user = new User(userDTO.getLogin());
        userService.save(user, userDTO.getPassword());
        return ResponseEntity.ok().build();
    }
}
