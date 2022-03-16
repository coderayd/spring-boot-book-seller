package com.work.bookseller.dto.request;

import com.work.bookseller.enumeration.Role;
import lombok.Data;
import javax.validation.constraints.NotNull;

@Data
public class UserRequestDto extends UserCreateRequestDto{

    @NotNull
    private Role role;
}
