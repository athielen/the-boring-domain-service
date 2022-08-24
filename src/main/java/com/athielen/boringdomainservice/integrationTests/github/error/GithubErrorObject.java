package com.athielen.boringdomainservice.integrationTests.github.error;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class GithubErrorObject {

    public String resource;

    public String field;

    public String code;

}
