package com.work.bookseller.controller;

import com.work.bookseller.dto.request.BookRequestDto;
import com.work.bookseller.dto.response.BaseResponseDto;
import com.work.bookseller.dto.response.BookResponseDto;
import com.work.bookseller.service.BookService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController{

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/{bookId}")
    public BaseResponseDto<BookResponseDto> getBook(@PathVariable Integer bookId) {
        return bookService.getBook(bookId);
    }

    @GetMapping
    public BaseResponseDto<List<BookResponseDto>> findAllBooks() {
        return bookService.findAllBooks();
    }

    @PostMapping
    public BaseResponseDto<BookResponseDto> createBook(@Valid @RequestBody BookRequestDto bookRequestDto) {
        return bookService.createBook(bookRequestDto);
    }

    @PutMapping("/{bookId}")
    public BaseResponseDto<BookResponseDto> updateBook(@PathVariable Integer bookId,
                                                       @Valid @RequestBody BookRequestDto bookRequestDto) {
        return bookService.updateBook(bookId, bookRequestDto);
    }

    @DeleteMapping("/{bookId}")
    public BaseResponseDto<?> deleteBook(@PathVariable Integer bookId) {
        return bookService.deleteBook(bookId);
    }
}
