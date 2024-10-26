package com.example.bookstore.repository;

import com.example.bookstore.entity.Favourites;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavouriteRepository extends JpaRepository<Favourites, Integer> {
}
