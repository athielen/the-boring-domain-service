package com.athielen.boringdomainservice.integrationTests.github.error;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GithubErrorResponse {

    public String message;

    public List<GithubErrorObject> errors;

}
