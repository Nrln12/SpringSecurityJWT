package com.bankofbaku.SpringSecurityJWT.api;

import com.bankofbaku.SpringSecurityJWT.entities.User;
import com.bankofbaku.SpringSecurityJWT.jwt.JwtTokenUtil;
import com.bankofbaku.SpringSecurityJWT.services.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class AuthApi {
    @Autowired
    AuthenticationManager  authenticationManager;

    @Autowired
    JwtTokenUtil jwtUtil;

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthRequest request){
        try{
            Authentication authentication =authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword())
            );
            User user= (User) authentication.getPrincipal();
            String accessToken=jwtUtil.generateAccessToken(user);
            AuthResponse response = new AuthResponse(user.getUsername(), accessToken);
            return ResponseEntity.ok(response);
        }catch (BadCredentialsException ex){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

    }

    public String encodePassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPsw = encoder.encode(password);
        return encodedPsw;
    }
}
