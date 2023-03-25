package com.koinmarket.app.exceptions.authentication;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class TokenNotGenerated extends RuntimeException{

    public TokenNotGenerated() {
        super("Token could not be generated");
    }

    public TokenNotGenerated(String message) {
        super(message);
    }
}
