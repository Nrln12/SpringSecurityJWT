package com.bankofbaku.SpringSecurityJWT.services;


import com.bankofbaku.SpringSecurityJWT.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    List<UserDto> getAllUsers();
    UserDto addUser(UserDto userDto) throws Exception;
    UserDto updateUser(Long id, UserDto userDto);
    void deleteUser(Long id);
    List<UserDto> findByPattern(String pattern);
}
