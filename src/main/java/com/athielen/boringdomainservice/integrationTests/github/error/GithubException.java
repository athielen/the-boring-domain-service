package com.athielen.boringdomainservice.integrationTests.github.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class GithubException extends ResponseStatusException {

    public GithubErrorResponse errorInfo;

    public GithubException(HttpStatus status, GithubErrorResponse errorInfo) {
        super(status);
        this.errorInfo = errorInfo;

    }

}
