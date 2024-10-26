package com.example.bookstore.service;

import com.example.bookstore.entity.Books;
import com.example.bookstore.entity.Genres;
import com.example.bookstore.model.enums.ParentGenres;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    private final GenreRepository genreRepository;

    public List<Books> getAllBooks() {
        return bookRepository.findAll(Sort.by(Sort.Direction.DESC, "publishYear"));
    }

    public List<Books> getBooksByCategories(ParentGenres parentGenres){
        List<Genres> genres = genreRepository.findByType(parentGenres);
        return bookRepository.findAll().stream()
                .filter(books -> books.getGenres().stream().anyMatch(genres::contains))
                .toList();
    }

    public Books getBookByIdAndSlug(Integer id, String slug) {
        return bookRepository.findBooksByIdAndSlug(id, slug);
    }

    public List<Books> getRecommendedBooks(List<ParentGenres> parentGenres, Integer id) {
        return bookRepository.findRecommendedBooksByParentGenres(parentGenres, id);
    }

    public Page<Books> getBooksByGenres(ParentGenres parentGenres,Boolean status, int page, int pageSize) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by("createdAt").descending());
        return bookRepository.findBooksByParentGenresAndStatus(parentGenres, pageRequest);
    }
}
