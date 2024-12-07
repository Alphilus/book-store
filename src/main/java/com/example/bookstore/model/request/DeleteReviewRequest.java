package com.example.bookstore.model.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeleteReviewRequest {
    Integer reviewId;  // The ID of the review to delete
}
