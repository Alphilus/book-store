package com.example.bookstore.repository;

import com.example.bookstore.entity.Volumes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VolumeRepository extends JpaRepository<Volumes, Integer> {
    List<Volumes> findByBooksIdOrderByDisplayOrder(Integer bookId);
}
