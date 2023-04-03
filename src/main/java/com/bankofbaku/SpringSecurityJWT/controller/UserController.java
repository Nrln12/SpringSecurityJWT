package com.bankofbaku.SpringSecurityJWT.controller;

import com.bankofbaku.SpringSecurityJWT.dto.UserDto;
import com.bankofbaku.SpringSecurityJWT.services.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/users/")
@RestController
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl userService;

    @GetMapping
    public List<UserDto> getAllUser(){
        return userService.getAllUsers();
    }

}
