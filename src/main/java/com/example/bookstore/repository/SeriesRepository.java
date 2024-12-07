package com.example.bookstore.repository;

import com.example.bookstore.entity.Books;
import com.example.bookstore.entity.Series;
import com.example.bookstore.entity.Volumes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SeriesRepository extends JpaRepository<Series, Integer> {
    boolean existsByBooksAndVolumes(Books books, Volumes volumes);

    @Query("SELECT s FROM Series s WHERE s.books.id = :bookId AND s.volumes.id = :volumeId")
    Optional<Series> findByBookIdAndVolumeId(@Param("bookId") Integer bookId, @Param("volumeId") Integer volumeId);
}
