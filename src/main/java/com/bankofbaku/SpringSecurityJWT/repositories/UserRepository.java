package com.bankofbaku.SpringSecurityJWT.repositories;

import com.bankofbaku.SpringSecurityJWT.dto.UserDto;
import com.bankofbaku.SpringSecurityJWT.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<com.bankofbaku.SpringSecurityJWT.entities.User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findById(Long id);
}
