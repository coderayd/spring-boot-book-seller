package com.work.bookseller.service.impl;

import com.work.bookseller.dto.request.BookRequestDto;
import com.work.bookseller.dto.response.BaseResponseDto;
import com.work.bookseller.dto.response.BookResponseDto;
import com.work.bookseller.exception.EntityNotFoundException;
import com.work.bookseller.model.Book;
import com.work.bookseller.repository.BookRepository;
import com.work.bookseller.service.BookService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private final ModelMapper modelMapper = new ModelMapper();
    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // https://www.baeldung.com/java-modelmapper-lists
    @Override
    public BaseResponseDto<List<BookResponseDto>> findAllBooks() {

        List<Book> books = bookRepository.findAll();

        // Teker teker tüm verileri mapper yapıyor, bu işlem performans olarak kötü gibime geldi.
        // direk List<Book> nesnesini döndürmek daha doğru bir çözüm olabilir.
        List<BookResponseDto> bookResponseDtoList = books.stream()
                .map(element -> modelMapper.map(element, BookResponseDto.class))
                .collect(Collectors.toList());

        return BaseResponseDto.<List<BookResponseDto>>builder()
                .code(HttpStatus.OK.value())
                .description("All The Books Have Been Brought")
                .data(bookResponseDtoList)
                .build();
    }

    @Override
    public BaseResponseDto<BookResponseDto> getBook(Integer id) {

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book Not Found"));

        return BaseResponseDto.<BookResponseDto>builder()
                .code(HttpStatus.OK.value())
                .description("Success")
                .data(modelMapper.map(book, BookResponseDto.class))
                .build();
    }

    @Override
    public BaseResponseDto<BookResponseDto> createBook(BookRequestDto bookRequestDto) {

        Book book = modelMapper.map(bookRequestDto, Book.class);
        book = bookRepository.save(book);

        return BaseResponseDto.<BookResponseDto>builder()
                .code(HttpStatus.OK.value())
                .description("Book Created")
                .data(modelMapper.map(book, BookResponseDto.class))
                .build();
    }

    @Transactional
    @Override
    public BaseResponseDto<BookResponseDto> updateBook(Integer id, BookRequestDto bookRequestDto) {

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book Not Found"));
        modelMapper.map(bookRequestDto, book);
        book = bookRepository.saveAndFlush(book);

        return BaseResponseDto.<BookResponseDto>builder()
                .code(HttpStatus.OK.value())
                .description("Book Updated")
                .data(modelMapper.map(book, BookResponseDto.class))
                .build();
    }

    @Transactional
    @Override
    public BaseResponseDto<?> deleteBook(Integer id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book Not Found"));
        bookRepository.delete(book);

        return BaseResponseDto.builder()
                .code(HttpStatus.OK.value())
                .description("Book Deleted")
                .build();
    }
}
