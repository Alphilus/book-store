package com.example.bookstore.model.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateReviewRequest {
    Integer reviewId;   // The ID of the review to update
    String content;     // New content for the review
    Integer rating;     // New rating for the review
}
