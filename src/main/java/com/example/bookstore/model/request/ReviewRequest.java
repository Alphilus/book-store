package com.example.bookstore.model.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewRequest {
    Integer userId;  // ID of the user creating the review
    Integer bookId;  // ID of the book being reviewed
    String content;  // Review content text
    Integer rating;  // Rating for the book, typically 1-10
}
