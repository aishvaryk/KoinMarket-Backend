package com.koinmarket.app.exceptions.metadata;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class MetadataNotFetched extends RuntimeException{

    public MetadataNotFetched() {
        super("Metadata not fetched");
    }

    public MetadataNotFetched(String message) {
        super(message);
    }
}
