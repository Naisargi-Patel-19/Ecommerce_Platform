package com.ecommerce.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserAlreadyExist extends Exception{
    public UserAlreadyExist(String errorMessage){
        super(errorMessage);
    }
}
