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

    public Optional<User> findByLoginAndPassword(String login, String password) {
        return  userRepository.findByLoginAndPassword(login, password);
    }

    public void save(User user, String password) {
        User createdUser = userRepository.save(user);
        userRepository.updatePassword(createdUser.getId(), password);
    }
}
