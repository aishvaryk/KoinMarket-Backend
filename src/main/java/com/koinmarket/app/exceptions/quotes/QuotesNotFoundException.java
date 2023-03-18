package com.koinmarket.app.exceptions.quotes;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class QuotesNotFoundException extends RuntimeException{

    public QuotesNotFoundException() {
        super("Quotes data not found");
    }

    public QuotesNotFoundException(String message) {
        super(message);
    }
}
