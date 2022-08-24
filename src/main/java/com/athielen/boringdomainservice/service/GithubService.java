package com.athielen.boringdomainservice.service;

import com.athielen.boringdomainservice.controller.v1.domain.GithubDetailsResponse;

public interface GithubService {

    GithubDetailsResponse getGithubAggregateInfo(String username);

}
