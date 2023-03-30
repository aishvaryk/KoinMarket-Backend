package com.koinmarket.app.exceptions;

import com.koinmarket.app.exceptions.cmcapi.CMCApiRequestError;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class CMCResponseErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse httpResponse)
            throws IOException {

        return  httpResponse.getStatusCode().isError();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        throw new CMCApiRequestError();
    }
}