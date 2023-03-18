package com.koinmarket.app.exceptions.listings;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class ListingsNotFetched extends RuntimeException{

    public ListingsNotFetched() {
        super("Listings not fetched");
    }

    public ListingsNotFetched(String message) {
        super(message);
    }
}
