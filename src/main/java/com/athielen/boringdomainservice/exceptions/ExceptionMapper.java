package com.athielen.boringdomainservice.exceptions;

import com.athielen.boringdomainservice.integrationTests.github.error.GithubErrorResponse;
import com.athielen.boringdomainservice.integrationTests.github.error.GithubException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionMapper {

    @ExceptionHandler(GithubException.class)
    ResponseEntity<GithubErrorResponse> processGithubException(GithubException ex) {
        // log a warning here
        return new ResponseEntity<GithubErrorResponse>(ex.errorInfo, ex.getStatus());
    }

    //TODO add a fallback with a fallback message for any unknown exceptions

}
