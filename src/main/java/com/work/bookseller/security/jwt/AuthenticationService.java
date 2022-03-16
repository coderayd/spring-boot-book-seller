package com.work.bookseller.security.jwt;

import com.work.bookseller.dto.request.UserLoginRequestDto;
import com.work.bookseller.dto.response.UserAuthResponseDto;

public interface AuthenticationService {

    UserAuthResponseDto signInAndReturnJWT(UserLoginRequestDto signInRequest);
}
