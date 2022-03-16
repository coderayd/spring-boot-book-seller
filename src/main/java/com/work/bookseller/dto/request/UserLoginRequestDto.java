package com.work.bookseller.dto.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class UserLoginRequestDto {

    @NotEmpty(message = "'username' can not be empty")
    @Size(min = 2, max = 50, message = "'username' should be 4 to 64 characters in length")
    private String username;

    @NotEmpty(message = "'password' can not be empty")
    @Size(min = 2, max = 100, message = "'password' should be 2 to 100 characters in length")
    private String password;
}
