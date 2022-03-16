package com.work.bookseller.controller;

import com.work.bookseller.dto.request.UserCreateRequestDto;
import com.work.bookseller.dto.request.UserRequestDto;
import com.work.bookseller.dto.response.BaseResponseDto;
import com.work.bookseller.dto.response.UserResponseDto;
import com.work.bookseller.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController{

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{username}")
    private BaseResponseDto<UserResponseDto> getUser(@PathVariable String username) {
        return userService.getUser(username);
    }

    @PutMapping("/{username}")
    private BaseResponseDto<UserResponseDto> updateUser(@PathVariable String username,
                                                        @Valid @RequestBody UserRequestDto userRequestDto) {
        return userService.updateUser(username, userRequestDto);
    }

    // Rolü sadece SYSTEM_MANAGER değiştirsin. ADMIN, USER -> 2 sinide SystemManager yapsın.
//    @PutMapping("/role-admin/{username}")
//    private BaseResponseDto<?> makeAdmin(@PathVariable String username){
//        return userService.makeAdmin(username);
//    }

    @DeleteMapping("/{username}")
    private BaseResponseDto<?> deleteUser(@PathVariable String username) {
        return userService.deleteUser(username);
    }
}
