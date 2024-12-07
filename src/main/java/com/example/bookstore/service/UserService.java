package com.example.bookstore.service;

import com.example.bookstore.entity.Books;
import com.example.bookstore.entity.Favourites;
import com.example.bookstore.entity.Users;
import com.example.bookstore.model.request.UpdatePasswordRequest;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.FavouriteRepository;
import com.example.bookstore.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final HttpSession session;
    private final PasswordEncoder passwordEncoder;
    private final BookRepository bookRepository;
    private final FavouriteRepository favouriteRepository;

    public Users getUserByNameAndEmail(Integer id, String email) {
        return userRepository.findUserByIdAndEmail(id, email);
    }

    public Integer getCurrentUserId() {
        Users user = (Users) session.getAttribute("currentUser");
        return user != null ? user.getId() : null;
    }

    public void updateUserProfile(Integer id, String name, String gender, Date dateOfBirth) {
        Users user = (Users) session.getAttribute("currentUser");
        if (user != null && user.getId().equals(id)) {
            user.setUserName(name);
            user.setGender(gender);
            user.setDateOfBirth(dateOfBirth);
            userRepository.save(user);

            // Update the session attribute
            session.setAttribute("currentUser", user);
        } else {
            throw new IllegalArgumentException("User not found or ID mismatch");
        }
    }

    public void updateUserPassword(Integer id, UpdatePasswordRequest request) {
        Users user = (Users) session.getAttribute("currentUser");
        if (user == null || !user.getId().equals(id)) {
            throw new IllegalArgumentException("User not found or ID mismatch");
        }

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("New passwords do not match");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    public List<Favourites> getFavoritesById(Integer userId) {
        return favouriteRepository.findByUsersId(userId);
    }

    public boolean addBookToFavourite(Integer userId, Integer bookId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Books book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));

        // Check if already in favorites
        if (favouriteRepository.existsByUsersAndBooks(user, book)) {
            return false; // Book is already a favorite
        }

        // Add to favorites
        Favourites favourite = new Favourites();
        favourite.setUsers(user);
        favourite.setBooks(book);
        favouriteRepository.save(favourite);
        return true;
    }

    @Transactional  
    public boolean removeBookFromFavourite(Integer userId, Integer bookId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Books book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));

        // Check if the favorite entry exists
        if (!favouriteRepository.existsByUsersAndBooks(user, book)) {
            log.info("Book with ID {} is not in user's favorites", bookId);
            return false; // Book is not in favorites
        }

        // Remove from favorites
        favouriteRepository.deleteByUsersAndBooks(user, book);
        log.info("Book with ID {} removed from user's favorites", bookId);
        return true;
    }


    public boolean isBookFavorite(Integer userId, Integer bookId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Books book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));

        return favouriteRepository.existsByUsersAndBooks(user, book);
    }
}
