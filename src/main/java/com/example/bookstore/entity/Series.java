package com.example.bookstore.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "series")
public class Series {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id", nullable = false) // References the 'id' of Books
    Books books;

    @ManyToOne
    @JoinColumn(name = "volume_id", referencedColumnName = "id", nullable = false) // References the 'id' of Volumes
    Volumes volumes;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
