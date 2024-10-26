package com.example.bookstore.repository;

import com.example.bookstore.entity.Reviews;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Reviews, Integer> {
    List<Reviews> findByBooks_IdOrderByCreatedAtDesc(Integer bookId);
}
