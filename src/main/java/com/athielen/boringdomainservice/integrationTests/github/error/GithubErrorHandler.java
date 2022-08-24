package com.athielen.boringdomainservice.integrationTests.github.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR;
import static org.springframework.http.HttpStatus.Series.SERVER_ERROR;

@Component
public class GithubErrorHandler implements ResponseErrorHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR ||
                response.getStatusCode().series() == SERVER_ERROR;
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        if (response.getStatusCode().series() == SERVER_ERROR) {
            handleErrors(response);
        } else if(response.getStatusCode().series() == CLIENT_ERROR) {
            handleErrors(response);
        } else {
            handleErrors(response);
        }
    }

    @Override
    public void handleError(URI url, HttpMethod method, ClientHttpResponse response) throws IOException {
        handleError(response);
    }

    private void handleErrors(ClientHttpResponse response) throws IOException {
        GithubErrorResponse errorResponse = buildErrorResponse(response);
        throw new GithubException(response.getStatusCode(), errorResponse);
    }

    private GithubErrorResponse buildErrorResponse(ClientHttpResponse response) throws IOException {
        return response.getBody().available() != 0 ?
                objectMapper.readValue(IOUtils.toString(response.getBody(), StandardCharsets.UTF_8.name()), GithubErrorResponse.class)
                : null;
    }

}
