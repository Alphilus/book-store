package com.example.bookstore.entity;

import com.example.bookstore.model.enums.BookType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "books")
public class Books {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    String title;

    String slug;

    @ManyToOne
    @JoinColumn(name = "author", nullable = false)
    Author author;

    @Column(columnDefinition = "TEXT")
    String description;

    String cover;

    String preview;

    Integer price;

    Double rating;

    Integer quantity;

    Integer publishYear;

    Boolean status;

    @Enumerated(EnumType.STRING)
    BookType type;

    @ManyToMany
    @JoinTable(
            name = "book_genres",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "genres_id")
    )
    List<Genres> genres;

    @ManyToOne
    @JoinColumn(name = "publisher_id", nullable = false)
    Publisher publisher;

    @ManyToOne
    @JoinColumn(name = "supplier_id", nullable = false)
    Supplier supplier;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
