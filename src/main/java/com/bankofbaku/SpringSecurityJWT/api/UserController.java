package com.bankofbaku.SpringSecurityJWT.api;

import com.bankofbaku.SpringSecurityJWT.dto.UserDto;
import com.bankofbaku.SpringSecurityJWT.repositories.UserRepository;
import com.bankofbaku.SpringSecurityJWT.services.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserServiceImpl userService;

    @GetMapping
    public List<UserDto> getAllUser(){
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserDto getUserById(Long id){
        return userService.getUserById(id);
    }
    @PostMapping("/add/user")
    public UserDto addUser(@RequestBody UserDto userDto) throws Exception {
        return userService.addUser(userDto);
    }

    @PutMapping("/update/user/{id}")
    public UserDto updateUser(@PathVariable Long id, @RequestBody UserDto userDto){
        return userService.updateUser(id, userDto);
    }

    @DeleteMapping("/delete/user/{id}")
    public void deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
    }

}
