package com.work.bookseller.dto.request;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class BookRequestDto {

    @NotEmpty(message = "'title' can not be empty")
    @Size(min = 2, max = 50, message = "'title' should be 2 to 50 characters in length")
    private String title;

    @NotEmpty(message = "'description' can not be empty")
    @Size(min = 10, max = 1000, message = "'description' should be 10 to 1000 characters in length")
    private String description;

    @NotEmpty(message = "'author' can not be empty")
    @Size(min = 2, max = 100, message = "'author' should be 2 to 100 characters in length")
    private String author;

    @NotNull(message = "'price' can not be null")
    @Min(value = 1, message = "'price' can be at least 1")
    private Double price;
}
