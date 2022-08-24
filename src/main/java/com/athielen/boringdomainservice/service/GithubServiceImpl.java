package com.athielen.boringdomainservice.service;

import com.athielen.boringdomainservice.controller.v1.domain.GithubDetailsResponse;
import com.athielen.boringdomainservice.integrationTests.github.GithubIntegration;
import com.athielen.boringdomainservice.integrationTests.github.domain.GithubRepoResponse;
import com.athielen.boringdomainservice.integrationTests.github.domain.GithubUsersDetailResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;

@Component
public class GithubServiceImpl implements GithubService {

    @Autowired
    private GithubIntegration githubIntegration;

    @Override
    public GithubDetailsResponse getGithubAggregateInfo(String username) {
        GithubUsersDetailResponse userDetails = githubIntegration.getGithubDetailsByUsername(username);
        GithubRepoResponse[] repoDetails = githubIntegration.getGithubReposByUsername(username);
        return mergeGithubResponses(username, userDetails, repoDetails);
    }

    private static GithubDetailsResponse mergeGithubResponses(String userName, GithubUsersDetailResponse userDetails,
                                                              GithubRepoResponse[] repoDetails) {
        if(userDetails == null) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new GithubDetailsResponse(userName, userDetails.displayName, userDetails.avatar, userDetails.geoLocation,
                userDetails.email, userDetails.url, userDetails.createdAt, repoDetails);
    }

}
