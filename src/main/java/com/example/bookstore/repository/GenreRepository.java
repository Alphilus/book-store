package com.example.bookstore.repository;

import com.example.bookstore.entity.Genres;
import com.example.bookstore.model.enums.ParentGenres;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GenreRepository extends JpaRepository<Genres, Integer> {
    List<Genres> findByType(ParentGenres parentGenres);
}
