package com.work.bookseller.service;

import com.work.bookseller.dto.request.UserCreateRequestDto;
import com.work.bookseller.dto.request.UserRequestDto;
import com.work.bookseller.dto.response.BaseResponseDto;
import com.work.bookseller.dto.response.UserResponseDto;

public interface UserService {

    BaseResponseDto<UserResponseDto> getUser(String username);

    BaseResponseDto<UserResponseDto> createUser(UserCreateRequestDto userCreateRequestDto);

    BaseResponseDto<UserResponseDto> updateUser(String username, UserRequestDto userRequestDto);

    BaseResponseDto<?> deleteUser(String username);

    // Bunu UserControllerde değil, InternalApiController de değiştiriyoruz.
    BaseResponseDto<?> makeAdmin(String username);
}
