package com.work.bookseller.service;

import com.work.bookseller.dto.request.BookRequestDto;
import com.work.bookseller.dto.response.BaseResponseDto;
import com.work.bookseller.dto.response.BookResponseDto;

import java.util.List;

public interface BookService {

    BaseResponseDto<List<BookResponseDto>> findAllBooks();

    BaseResponseDto<BookResponseDto> getBook(Integer id);

    BaseResponseDto<BookResponseDto> createBook(BookRequestDto bookRequestDto);

    BaseResponseDto<BookResponseDto> updateBook(Integer id, BookRequestDto bookRequestDto);

    BaseResponseDto<?> deleteBook(Integer id);
}