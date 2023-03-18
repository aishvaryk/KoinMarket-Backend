package com.koinmarket.app.exceptions.quotes;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class QuotesNotFetched extends RuntimeException{

    public QuotesNotFetched() {
        super("Quotes not fetched");
    }

    public QuotesNotFetched(String message) {
        super(message);
    }
}
