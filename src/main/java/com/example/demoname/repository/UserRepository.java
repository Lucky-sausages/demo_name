package com.example.demoname.repository;

import com.example.demoname.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findById(Integer id);

    Optional<User> findByLogin(String login);

    User save(User user);
}
