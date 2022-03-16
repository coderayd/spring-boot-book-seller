package com.work.bookseller.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.work.bookseller.model.Book;
import com.work.bookseller.model.User;
import lombok.Data;

import java.util.Date;

@Data
public class PurchaseResponseDto {

    private Integer id;
    private Double price;

    @JsonFormat(pattern="yyyy-MM-dd")
    //@ApiModelProperty(example = "2022-01-09")
    private Date createdDate;

    private UserResponseDto user;
    private BookResponseDto book;

}
