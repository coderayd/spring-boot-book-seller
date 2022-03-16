package com.work.bookseller.service.impl;

import com.work.bookseller.dto.request.UserCreateRequestDto;
import com.work.bookseller.dto.request.UserRequestDto;
import com.work.bookseller.dto.response.BaseResponseDto;
import com.work.bookseller.dto.response.UserResponseDto;
import com.work.bookseller.enumeration.Role;
import com.work.bookseller.exception.EntityNotFoundException;
import com.work.bookseller.model.User;
import com.work.bookseller.repository.UserRepository;
import com.work.bookseller.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private final ModelMapper modelMapper = new ModelMapper();
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public BaseResponseDto<UserResponseDto> getUser(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User Not Found"));

        return BaseResponseDto.<UserResponseDto>builder()
                .code(HttpStatus.OK.value())
                .description("Success")
                .data(modelMapper.map(user, UserResponseDto.class))
                .build();
    }

    @Override
    public BaseResponseDto<UserResponseDto> createUser(UserCreateRequestDto userCreateRequestDto) {

        User user = modelMapper.map(userCreateRequestDto, User.class);
        user.setPassword(bCryptPasswordEncoder.encode(userCreateRequestDto.getPassword()));
        user.setRole(Role.USER);
        user = userRepository.save(user);

        return BaseResponseDto.<UserResponseDto>builder()
                .code(HttpStatus.OK.value())
                .description("User Created")
                .data(modelMapper.map(user, UserResponseDto.class))
                .build();
    }

    @Transactional
    @Override
    public BaseResponseDto<UserResponseDto> updateUser(String username, UserRequestDto userRequestDto) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User Not Found"));
        modelMapper.map(userRequestDto, user);
        user = userRepository.saveAndFlush(user);

        return BaseResponseDto.<UserResponseDto>builder()
                .code(HttpStatus.OK.value())
                .description("User Updated")
                .data(modelMapper.map(user, UserResponseDto.class))
                .build();
    }

    @Transactional
    @Override
    public BaseResponseDto<?> deleteUser(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Username Not Found"));
        userRepository.delete(user);

        return BaseResponseDto.builder()
                .code(HttpStatus.OK.value())
                .description("User Deleted")
                .build();
    }

    @Transactional
    @Override
    public BaseResponseDto<?> makeAdmin(String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User Not Found"));
        userRepository.updateUserRole(username, Role.ADMIN);

        return BaseResponseDto.builder()
                .code(HttpStatus.OK.value())
                .description("User role changed to ADMIN")
                .build();
    }
}
