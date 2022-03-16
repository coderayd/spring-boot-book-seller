package com.work.bookseller.dto.response;

import lombok.Data;

@Data
public class BookResponseDto {

    private Integer id;
    private String title;
    private String description;
    private String author;
    private Double price;
}
