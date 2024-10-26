package com.example.bookstore.repository;

import com.example.bookstore.entity.TokenConfirm;
import com.example.bookstore.model.enums.TokenType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenConfirmRepository extends JpaRepository<TokenConfirm, Integer> {
    Optional<TokenConfirm> findByTokenAndType(String token, TokenType type);
}
