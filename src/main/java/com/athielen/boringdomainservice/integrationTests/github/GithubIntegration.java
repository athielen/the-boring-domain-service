package com.athielen.boringdomainservice.integrationTests.github;

import com.athielen.boringdomainservice.integrationTests.github.domain.GithubRepoResponse;
import com.athielen.boringdomainservice.integrationTests.github.domain.GithubUsersDetailResponse;

public interface GithubIntegration {

    GithubUsersDetailResponse getGithubDetailsByUsername(String username);

    GithubRepoResponse[] getGithubReposByUsername(String username);

}
