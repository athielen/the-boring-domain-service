package com.athielen.boringdomainservice.integrationTests.github;

import com.athielen.boringdomainservice.integrationTests.github.domain.GithubRepoResponse;
import com.athielen.boringdomainservice.integrationTests.github.domain.GithubUsersDetailResponse;
import com.athielen.boringdomainservice.integrationTests.github.error.GithubErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.*;
import java.util.function.Supplier;

import static com.athielen.boringdomainservice.utils.RestTemplateUtil.execute;

@Component
public class GithubRestTemplate implements GithubIntegration {

    private static final Map<String, String> EMPTY_MAP = Collections.emptyMap();

    private static final String USERNAME = "username";

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    @Autowired
    private GithubErrorHandler errorHandler;

    @Value("${boring.domain.service.github.client.base.url}")
    private String baseUrl;

    @Value("${boring.domain.service.github.client.mock.url}")
    private String mockBaseUrl;

    @Value("${boring.domain.service.github.client.paths.users.uri}")
    private String usersUri;

    @Value("${boring.domain.service.github.client.paths.userRepos.uri}")
    private String reposUri;

    @Value("${environment:dev}")
    private String environment;

    private RestTemplate restTemplate;

    private Supplier<String> getBaseUrl;

    @PostConstruct
    private void init() {
        restTemplate = restTemplateBuilder
                .setConnectTimeout(Duration.ofMillis(5000))
                .setReadTimeout(Duration.ofMillis(5000))
                .errorHandler(errorHandler)
                .build();
    }

    // TODO: add a circuit breaker
    public GithubUsersDetailResponse getGithubDetailsByUsername(String username) {
        Map<String, String> uriVariables = Map.of(USERNAME, username);
        return execute(restTemplate, HttpMethod.GET, baseUrl, usersUri, EMPTY_MAP , uriVariables, getHeaders(),
                null, GithubUsersDetailResponse.class);
    }

    // TODO: add a circuit breaker
    public GithubRepoResponse[] getGithubReposByUsername(String username) {
        Map<String, String> uriVariables = Map.of(USERNAME, username);
        return execute(restTemplate, HttpMethod.GET, baseUrl, reposUri, EMPTY_MAP , uriVariables, getHeaders(),
                null, GithubRepoResponse[].class);
    }

    private Map<String, String> getHeaders() {
        return Map.of("content-type", MediaType.APPLICATION_JSON_VALUE);
    }

}
