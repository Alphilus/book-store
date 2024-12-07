package com.example.bookstore.controller;

import com.example.bookstore.entity.*;
import com.example.bookstore.model.enums.BookType;
import com.example.bookstore.model.enums.ParentGenres;
import com.example.bookstore.response.VerifyResponse;
import com.example.bookstore.service.*;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class WebController {
    private final BookService bookService;

    private final VolumeService volumeService;

    private final ReviewService reviewService;

    private final AuthService authService;

    private final UserService userService;

    @GetMapping("/login")
    public String getLogin() {
        return "/html/login";
    }

    @GetMapping("/register")
    public String getRegister() {
        return "/html/register";
    }

    // http://localhost:8080/xac-thuc-tai-khoan?token=298e0780-fb22-44bc-90b9-5d3b333e25f4
//    @GetMapping("/confirmation")
//    public String getConfirmRegistrationPage(@RequestParam String token, Model model) {
//        VerifyResponse response = authService.confirmRegistration(token);
//        model.addAttribute("response", response);
//        return "/html/confirm-registration";
//    }

    @GetMapping("/user/{id}/{email}")
    public String getUserPage(Model model, @PathVariable Integer id, @PathVariable String email) {
        Users users = userService.getUserByNameAndEmail(id, email);
        model.addAttribute("users", users);
        return "/user/profile-user";
    }

    @GetMapping("/user/{id}/{email}/favourite")
    public String getFavoriteBooks(Model model, @PathVariable Integer id, @PathVariable String email) {
        Users user = userService.getUserByNameAndEmail(id, email);

        if (user == null) {
            return "html/index";
        }

        // Retrieve the list of favorite books, assuming it's a list of Favourites
        List<Favourites> favouriteBooks = userService.getFavoritesById(user.getId());

        // Extract the books from the Favourites list
        List<Books> books = favouriteBooks.stream()
                .map(Favourites::getBooks) // Get the associated book
                .collect(Collectors.toList());

        model.addAttribute("user", user);
        model.addAttribute("favouriteBooks", books);  // Pass the list of books to the view

        return "/user/favourite";
    }

    @GetMapping("/")
    public String getHomePage(Model model,
                              @RequestParam(required = false, defaultValue = "1") int page,
                              @RequestParam(required = false, defaultValue = "12") int pageSize) {
        List<Books> books = bookService.getAllBooks();
        List<Books> getTextBooks = bookService.getBooksByCategories(ParentGenres.TEXTBOOKS);
        List<Books> getScience = bookService.getBooksByCategories(ParentGenres.SCIENCE);
        List<Books> getHistory = bookService.getBooksByCategories(ParentGenres.HISTORY);
        List<Books> getBio = bookService.getBooksByCategories(ParentGenres.BIOGRAPHY);
        List<Books> getFiction = bookService.getBooksByCategories(ParentGenres.FICTION);
        List<Books> getNonFiction = bookService.getBooksByCategories(ParentGenres.NON_FICTION);
        model.addAttribute("books", books);
        model.addAttribute("getTextBooks", getTextBooks);
        model.addAttribute("getScience", getScience);
        model.addAttribute("getHistory", getHistory);
        model.addAttribute("getBio", getBio);
        model.addAttribute("getFiction", getFiction);
        model.addAttribute("getNonFiction", getNonFiction);
        return "/html/index";
    }

    @GetMapping("/categories")
    public String getCategories(Model model,
                                @RequestParam(required = false, defaultValue = "1") int page,
                                @RequestParam(required = false, defaultValue = "12") int pageSize) {
        return "/html/categories";
    }

    @GetMapping("/blog")
    public String getBlog(Model model,
                           @RequestParam(required = false, defaultValue = "1") int page,
                           @RequestParam(required = false, defaultValue = "12") int pageSize) {
        return "/html/blog";
    }

    @GetMapping("/about")
    public String getAbout(Model model,
                           @RequestParam(required = false, defaultValue = "1") int page,
                           @RequestParam(required = false, defaultValue = "12") int pageSize) {
        return "/html/about";
    }

    @GetMapping("/contact")
    public String getContact(Model  model,
                             @RequestParam(required = false, defaultValue = "1") int page,
                             @RequestParam(required = false, defaultValue = "12") int pageSize) {
        return "/html/contact";
    }

    @GetMapping("/book/{id}/{slug}")
    public String getDetail(Model model, @PathVariable Integer id, @PathVariable String slug) {
        Books books = bookService.getBookByIdAndSlug(id, slug);
        Integer userId = userService.getCurrentUserId();


        if (books == null){
            return "redirect:/not-found";
        }

        if (books.getType() == BookType.SERIES) {
            List<Volumes> volumes = volumeService.getVolumeByBookId(books.getId());
            System.out.println("Volume count: " + volumes.size()); // For debugging
            model.addAttribute("volumes", volumes);
        }

        List<Reviews> reviews = reviewService.getReviewsByBookId(id);

        List<Genres> genres = books.getGenres();
        List<ParentGenres> parentGenres = genres.stream()
                .map(Genres::getType)
                .distinct()
                .toList();

        List<Books> recommendedBooks = bookService.getRecommendedBooks(parentGenres, books.getId());

        // Debugging: Output the size of the recommended books list
        System.out.println("Recommended Books Count: " + recommendedBooks.size());

        boolean isFavorite = userId != null && userService.isBookFavorite(userId, books.getId());

        model.addAttribute("isFavorite", isFavorite);
        model.addAttribute("books", books);
        model.addAttribute("reviews", reviews);
        model.addAttribute("recommendedBooks", recommendedBooks);
        model.addAttribute("parentGenres", parentGenres);
        return "/html/book-detail";
    }

    @GetMapping("/genres/fiction")
    public String getFiction(Model model,
                             @RequestParam(required = false, defaultValue = "1") int page,
                             @RequestParam(required = false, defaultValue = "12") int pageSize){
        Page<Books> booksPage = bookService.getBooksByGenres(ParentGenres.FICTION, true, page, pageSize);
        model.addAttribute("booksPage", booksPage);
        model.addAttribute("currentPage", page);
        return "/html/fiction";
    }

    @GetMapping("/genres/non_fiction")
    public String getNonFiction(Model model,
                             @RequestParam(required = false, defaultValue = "1") int page,
                             @RequestParam(required = false, defaultValue = "12") int pageSize){
        Page<Books> booksPage = bookService.getBooksByGenres(ParentGenres.NON_FICTION, true, page, pageSize);
        model.addAttribute("booksPage", booksPage);
        model.addAttribute("currentPage", page);
        return "/html/non-fiction";
    }

    @GetMapping("/genres/textbooks")
    public String getTextBook(Model model,
                                @RequestParam(required = false, defaultValue = "1") int page,
                                @RequestParam(required = false, defaultValue = "12") int pageSize){
        Page<Books> booksPage = bookService.getBooksByGenres(ParentGenres.TEXTBOOKS, true, page, pageSize);
        model.addAttribute("booksPage", booksPage);
        model.addAttribute("currentPage", page);
        return "/html/textbooks";
    }

    @GetMapping("/genres/science")
    public String getScience(Model model,
                                @RequestParam(required = false, defaultValue = "1") int page,
                                @RequestParam(required = false, defaultValue = "12") int pageSize){
        Page<Books> booksPage = bookService.getBooksByGenres(ParentGenres.SCIENCE, true, page, pageSize);
        model.addAttribute("booksPage", booksPage);
        model.addAttribute("currentPage", page);
        return "/html/science";
    }

    @GetMapping("/genres/history")
    public String getHistory(Model model,
                                @RequestParam(required = false, defaultValue = "1") int page,
                                @RequestParam(required = false, defaultValue = "12") int pageSize){
        Page<Books> booksPage = bookService.getBooksByGenres(ParentGenres.HISTORY, true, page, pageSize);
        model.addAttribute("booksPage", booksPage);
        model.addAttribute("currentPage", page);
        return "/html/history";
    }

    @GetMapping("/genres/biography")
    public String getBio(Model model,
                                @RequestParam(required = false, defaultValue = "1") int page,
                                @RequestParam(required = false, defaultValue = "12") int pageSize){
        Page<Books> booksPage = bookService.getBooksByGenres(ParentGenres.BIOGRAPHY, true, page, pageSize);
        model.addAttribute("booksPage", booksPage);
        model.addAttribute("currentPage", page);
        return "/html/biography";
    }
}