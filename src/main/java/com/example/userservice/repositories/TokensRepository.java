package com.example.userservice.repositories;

import com.example.userservice.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Optional;

@Repository
public interface TokensRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByValueAndActiveEquals(String token, boolean isActive);
    Optional<Token> findByValueAndActiveEqualsAndExpireAtGreaterThanEqual(String value, boolean active, Date expiryDate);
}
