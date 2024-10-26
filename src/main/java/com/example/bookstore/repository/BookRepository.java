package com.example.bookstore.repository;

import com.example.bookstore.entity.Books;
import com.example.bookstore.model.enums.ParentGenres;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Books, Integer> {
    Books findBooksByIdAndSlug(Integer id, String slug);
    List<Books> findBooksByStatus(boolean status);

    @Query("SELECT DISTINCT b FROM Books b " +
            "JOIN b.genres g " +
            "WHERE g.type IN :parentGenres AND b.id <> :id " +
            "ORDER BY b.rating DESC")
    List<Books> findRecommendedBooksByParentGenres(@Param("parentGenres") List<ParentGenres> parentGenres, @Param("id") Integer id);

    @Query("SELECT b FROM Books b " +
            "JOIN b.genres g " +
            "WHERE g.type = :parentGenres")
    Page<Books> findBooksByParentGenresAndStatus(@Param("parentGenres") ParentGenres parentGenres, Pageable pageable);
}
