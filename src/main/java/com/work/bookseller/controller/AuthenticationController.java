package com.work.bookseller.controller;

import com.work.bookseller.dto.request.UserCreateRequestDto;
import com.work.bookseller.dto.request.UserLoginRequestDto;
import com.work.bookseller.dto.response.BaseResponseDto;
import com.work.bookseller.dto.response.UserAuthResponseDto;
import com.work.bookseller.dto.response.UserResponseDto;
import com.work.bookseller.security.jwt.AuthenticationService;
import com.work.bookseller.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    public AuthenticationController(AuthenticationService authenticationService, UserService userService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @PostMapping("/sign-up")
    private BaseResponseDto<UserResponseDto> signUp(@Valid @RequestBody UserCreateRequestDto userCreateRequestDto) {
        return userService.createUser(userCreateRequestDto);
    }

    @PostMapping("/sign-in")
    public BaseResponseDto<UserAuthResponseDto> signIn(@Valid @RequestBody UserLoginRequestDto userLoginRequestDto){

        return BaseResponseDto.<UserAuthResponseDto>builder()
                .code(HttpStatus.OK.value())
                .description("Success")
                .data(authenticationService.signInAndReturnJWT(userLoginRequestDto))
                .build();
    }

}
