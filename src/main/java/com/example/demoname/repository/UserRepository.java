package com.example.demoname.repository;

import com.example.demoname.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT * FROM users WHERE login=?1 AND passwordSha=SHA1(?2)", nativeQuery = true)
    Optional<User> findByLoginAndPassword(String login, String password);

    @Transactional
    @Modifying
    @Query(value = "UPDATE users SET passwordSha=SHA1(?2) WHERE id=?1", nativeQuery = true)
    void updatePassword(long id, String password);

}
