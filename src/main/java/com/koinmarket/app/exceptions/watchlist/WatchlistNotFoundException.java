package com.koinmarket.app.exceptions.watchlist;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class WatchlistNotFoundException extends RuntimeException{

    public WatchlistNotFoundException() {
        super("Watchlist not found");
    }

    public WatchlistNotFoundException(String message) {
        super(message);
    }
}
