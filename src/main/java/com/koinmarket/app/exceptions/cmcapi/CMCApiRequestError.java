package com.koinmarket.app.exceptions.cmcapi;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class CMCApiRequestError extends RuntimeException{

    public CMCApiRequestError() {
        super("Internal Data Fetching Error");
    }

    public CMCApiRequestError(String message) {
        super(message);
    }
}