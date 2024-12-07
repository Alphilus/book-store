package com.example.bookstore.rest;

import com.example.bookstore.model.request.*;
import com.example.bookstore.service.ReviewService;
import com.example.bookstore.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserApi {
    private final UserService userService;
    private final ReviewService reviewService;

    @PutMapping("/update-profile")
    public ResponseEntity<?> updateProfile(@RequestBody UpdateUserProfileRequest request) {
        try {
            Integer userId = userService.getCurrentUserId();
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            userService.updateUserProfile(userId, request.getName(), request.getGender(), request.getDateOfBirth());
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/update-password")
    public ResponseEntity<?> updatePassword(@RequestBody UpdatePasswordRequest request) {
        try {
            Integer userId = userService.getCurrentUserId();

            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            userService.updateUserPassword(userId, request);

            return ResponseEntity.ok("Password updated successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/add-favourite")
    public ResponseEntity<?> addFavourite(@RequestBody CreateFavouriteRequest request) {
        try {
            Integer userId = userService.getCurrentUserId();

            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated.");
            }

            boolean added = userService.addBookToFavourite(userId, request.getBookId());

            if (added) {
                return ResponseEntity.ok("Book added to favorites successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Book is already in favorites.");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while adding to favorites.");
        }
    }

    @PutMapping("/remove-favourite")
    public ResponseEntity<?> removeFavourite(@RequestBody DeleteFavouriteRequest request) {
        try {
            Integer userId = userService.getCurrentUserId();
            Integer bookId = request.getBookId();

            // Debugging logs to check values
            log.info("Attempting to remove favourite - User ID: {}, Book ID: {}", userId, bookId);

            if (userId == null) {
                log.error("User is not authenticated.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated.");
            }

            boolean removed = userService.removeBookFromFavourite(userId, bookId);

            if (removed) {
                log.info("Book successfully removed from favourites.");
                return ResponseEntity.ok("Book removed from favourites successfully.");
            } else {
                log.warn("Book is not in favourites.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book is not in favourites.");
            }
        } catch (IllegalArgumentException e) {
            log.error("Invalid input: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            log.error("An unexpected error occurred: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while removing from favourites.");
        }
    }

    @PostMapping("/reviews/create")
    public ResponseEntity<String> createReview(@RequestBody ReviewRequest reviewRequest) {
        reviewService.createReview(reviewRequest);
        return ResponseEntity.ok("Review created successfully");
    }
}
