package com.example.bookstore.repository;

import com.example.bookstore.entity.Books;
import com.example.bookstore.entity.Favourites;
import com.example.bookstore.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavouriteRepository extends JpaRepository<Favourites, Integer> {
    List<Favourites> findByUsersId(Integer userId);
    boolean existsByUsersAndBooks(Users users, Books books);
    void deleteByUsersAndBooks(Users users, Books books);
}
