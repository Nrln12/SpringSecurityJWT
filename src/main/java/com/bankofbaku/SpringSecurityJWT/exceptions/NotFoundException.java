package com.bankofbaku.SpringSecurityJWT.exceptions;

import org.aspectj.weaver.ast.Not;
import org.springframework.http.HttpStatus;

import java.util.Map;

public class NotFoundException extends CustomException{
    public NotFoundException(String message){
        super(message, HttpStatus.NOT_FOUND);
    }
    public NotFoundException(String message, Map<String, String> params){
        super(message, HttpStatus.NOT_FOUND,params);
    }
}
