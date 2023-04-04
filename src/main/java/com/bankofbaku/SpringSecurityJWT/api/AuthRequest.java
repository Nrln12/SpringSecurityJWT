package com.bankofbaku.SpringSecurityJWT.api;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
public class AuthRequest {
    private String username;
    private String password;
}
