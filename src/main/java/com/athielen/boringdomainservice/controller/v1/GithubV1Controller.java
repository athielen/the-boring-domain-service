package com.athielen.boringdomainservice.controller.v1;

import com.athielen.boringdomainservice.controller.v1.domain.GithubDetailsResponse;
import com.athielen.boringdomainservice.service.GithubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;

@RestController
@RequestMapping("/v1/github/wrapper/")
public class GithubV1Controller {

    @Autowired
    private GithubService githubService;

    @GetMapping(value = "/name/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public GithubDetailsResponse getGithubDetailsByUsername(@PathVariable("username") @NotEmpty String username) {
        return githubService.getGithubAggregateInfo(username);
    }

}
