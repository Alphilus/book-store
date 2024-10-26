package com.example.bookstore.service;

import com.example.bookstore.entity.Reviews;
import com.example.bookstore.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public List<Reviews> getReviewsByBookId(Integer id) {
        return reviewRepository.findByBooks_IdOrderByCreatedAtDesc(id);
    }
}
