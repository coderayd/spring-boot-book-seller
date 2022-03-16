package com.work.bookseller.dto.response;

import com.work.bookseller.enumeration.Role;
import lombok.Data;

@Data
public class UserResponseDto {

    private Integer id;
    private String username;
    private String name;
    private Role role;
}
