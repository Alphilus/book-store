package com.example.bookstore.service;

import com.example.bookstore.entity.Books;
import com.example.bookstore.entity.Reviews;
import com.example.bookstore.entity.Users;
import com.example.bookstore.model.request.ReviewRequest;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.ReviewRepository;
import com.example.bookstore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public List<Reviews> getReviewsByBookId(Integer id) {
        return reviewRepository.findByBooks_IdOrderByCreatedAtDesc(id);
    }

    public void createReview(ReviewRequest reviewRequest) {
        Users user = userRepository.findById(reviewRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Books book = bookRepository.findById(reviewRequest.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found"));

        Reviews review = Reviews.builder()
                .content(reviewRequest.getContent())
                .rating(reviewRequest.getRating())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .users(user)
                .books(book)
                .build();

        reviewRepository.save(review);
    }

}
