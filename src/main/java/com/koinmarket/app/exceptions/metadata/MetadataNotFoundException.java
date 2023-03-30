package com.koinmarket.app.exceptions.metadata;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class MetadataNotFoundException extends RuntimeException{

    public MetadataNotFoundException() {
        super("Metadata not found");
    }

    public MetadataNotFoundException(String message) {
        super(message);
    }
}
