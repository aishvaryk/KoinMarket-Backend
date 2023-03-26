package com.koinmarket.app.exceptions.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class UserAlreadyExistException extends RuntimeException{

    public UserAlreadyExistException() {
        super("User already exists");
    }

    public UserAlreadyExistException(String message) {
        super(message);
    }
}
