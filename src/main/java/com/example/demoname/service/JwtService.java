package com.example.demoname.service;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demoname.domain.User;
import com.example.demoname.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JwtService {

    private UserRepository userRepository;

    public JwtService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Value("${jwt.secret}")
    private String SECRET;

    public Optional<User> find(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET))
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return userRepository.findById(jwt.getClaims().get("userId").asLong());
        } catch (JWTVerificationException ignored){
            return Optional.empty();
        }
    }

    public String create(User user) {
        try {
            return JWT.create()
                    .withClaim("userId", user.getId())
                    .sign(Algorithm.HMAC256(SECRET));
        } catch (JWTCreationException exception){
            throw new RuntimeException("Can't create JWT.", exception);
        }
    }

}
