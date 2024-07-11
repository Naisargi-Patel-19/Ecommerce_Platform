package com.ecommerce.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotExist extends Exception{
    public UserNotExist(String errorMessage){
        super(errorMessage);

    }
}
