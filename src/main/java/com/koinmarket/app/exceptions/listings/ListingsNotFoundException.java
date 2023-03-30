package com.koinmarket.app.exceptions.listings;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ListingsNotFoundException extends RuntimeException{

    public ListingsNotFoundException() {
        super("Listings Data not found");
    }

    public ListingsNotFoundException(String message) {
        super(message);
    }
}
