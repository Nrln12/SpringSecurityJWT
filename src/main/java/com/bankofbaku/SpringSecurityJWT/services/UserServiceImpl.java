package com.bankofbaku.SpringSecurityJWT.services;

import com.bankofbaku.SpringSecurityJWT.dto.UserDto;
import com.bankofbaku.SpringSecurityJWT.entities.User;
import com.bankofbaku.SpringSecurityJWT.exceptions.BadRequestException;
import com.bankofbaku.SpringSecurityJWT.exceptions.IsNotValidException;
import com.bankofbaku.SpringSecurityJWT.exceptions.NotFoundException;
import com.bankofbaku.SpringSecurityJWT.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = new ModelMapper();
    }

    @Override
    public List<UserDto> getAllUsers() {

        List<UserDto> users = userRepository.findAll().stream().map(user -> modelMapper.map(user, UserDto.class)).collect(Collectors.toList());
        if (users.size()==0) {
            throw new NotFoundException("No data found");
        } else {
            return users;
        }
    }

    @Override
    public UserDto addUser(UserDto userDto) throws Exception {
        if (hasSame(userDto.getUsername())) {
            throw new BadRequestException("This username has already taken");
        }
        try {
            if (!isValidUsername(userDto.getUsername())) {
                throw new IsNotValidException("Username is not valid");
            }
            if (!isValidPassword(userDto.getPassword())) {
                throw new IsNotValidException("Password is not valid");
            }
            userDto.setPassword(encodePassword(userDto.getPassword()));
            userRepository.save(modelMapper.map(userDto, User.class));
        } catch (Exception ex) {
            throw new Exception(ex);
        }
        return userDto;
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        if (!exists(id)) {
            throw new NotFoundException("User doesn't exist");
        }
        Optional<User> user = userRepository.findById(id);
        if (!isValidPassword(userDto.getPassword())) {
            throw new IsNotValidException("Password is not valid");
        }
        if (!isValidUsername(userDto.getUsername())) {
            throw new IsNotValidException("Username is not valid");
        }
        user.get().setUsername(userDto.getUsername());
        user.get().setPassword(encodePassword(userDto.getPassword()));
        userRepository.save(user.get());
        return modelMapper.map(userRepository.findById(id), UserDto.class);
    }

    @Override
    public void deleteUser(Long id) {
       if(!exists(id)){
           throw new NotFoundException("User doesn't exist");
       }
       Optional<User> user = userRepository.findById(id);
       userRepository.deleteById(id);
    }

    @Override
    public List<UserDto> findByPattern(String pattern) {
        List<UserDto> matchedEntities=new ArrayList<>();
        for(UserDto userDto : getAllUsers()){
            if(userDto.getUsername().contains(pattern)){
                matchedEntities.add(userDto);
            }
        }
        return matchedEntities;
    }

    public UserDto getUserById(Long id){
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            return modelMapper.map(user.get(), UserDto.class);
        }
        throw new NotFoundException("User doesn't exist");

    }
    private boolean isValidUsername(String username) {
        String regex = "^[a-zA-Z0-9._-]{3,}$";
        return username.matches(regex) ? true : false;
    }

    private boolean isValidPassword(String password) {
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$";
        return password.matches(regex) ? true : false;
    }

    public String encodePassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPsw = encoder.encode(password);
        return encodedPsw;
    }

    private boolean hasSame(String username) {
        if (userRepository.findByUsername(username).isEmpty())
            return true;
        else return false;
    }

    private boolean exists(Long id) {
        if (userRepository.findById(id).isPresent()) return true;
        return false;
    }
}
