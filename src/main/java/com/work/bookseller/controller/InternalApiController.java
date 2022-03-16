package com.work.bookseller.controller;

import com.work.bookseller.dto.response.BaseResponseDto;
import com.work.bookseller.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Swagger içinde token verildiğinde null geliyordu. Bu sebeple @Security... eklendi
// https://stackoverflow.com/questions/59898874/enable-authorize-button-in-springdoc-openapi-ui-for-bearer-token-authentication

@RestController
@RequestMapping("/api/internal")
@SecurityRequirement(name = "bearerAuth")
public class InternalApiController {

    private final UserService userService;

    public InternalApiController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/make-admin/{username}")
    private BaseResponseDto<?> makeAdmin(@PathVariable String username){

        return userService.makeAdmin(username);
    }

}