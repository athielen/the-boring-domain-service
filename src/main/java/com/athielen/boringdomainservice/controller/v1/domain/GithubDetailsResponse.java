package com.athielen.boringdomainservice.controller.v1.domain;

import com.athielen.boringdomainservice.integrationTests.github.domain.GithubRepoResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

public class GithubDetailsResponse {

    @JsonProperty("user_name")
    public String userName;

    @JsonProperty("display_name")
    public String displayName;

    public String avatar;

    @JsonProperty("geo_location")
    public String geoLocation;

    public String email;

    public String url;

    @JsonProperty("created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date createdAt;

    public GithubRepoResponse[] repos;

    public GithubDetailsResponse() {
        //intentionally left empty
    }

    public GithubDetailsResponse(String userName, String displayName, String avatar, String geoLocation,
                                 String email, String url, Date createdAt, GithubRepoResponse[] repos) {
        this.userName = userName;
        this.displayName = displayName;
        this.avatar = avatar;
        this.geoLocation = geoLocation;
        this.email = email;
        this.url = url;
        this.createdAt = createdAt;
        this.repos = repos;
    }

}
