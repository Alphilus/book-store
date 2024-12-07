package com.example.bookstore.repository;

import com.example.bookstore.entity.Users;
import com.example.bookstore.model.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Integer> {
    List<Users> findUsersByRole(UserRole role);
    Users findUsersByUserName(String username);
    Optional<Users> findByEmail(String email);
    Users findUserByIdAndEmail(Integer id, String email);
}
