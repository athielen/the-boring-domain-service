package com.athielen.boringdomainservice.integrationTests.github.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GithubUsersDetailResponse {

    @JsonProperty("name")
    public String displayName;

    @JsonProperty("avatar_url")
    public String avatar;

    @JsonProperty("location")
    public String geoLocation;

    public String email;

    @JsonProperty("html_url")
    public String url;

    @JsonProperty("created_at")
    public Date createdAt;

}
