package com.example.demoname.service;

import com.example.demoname.domain.User;
import com.example.demoname.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findByLogin(String login) {
        return  userRepository.findByLogin(login);

    }
}
