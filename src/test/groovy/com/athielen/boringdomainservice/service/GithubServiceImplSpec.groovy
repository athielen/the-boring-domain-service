package com.athielen.boringdomainservice.service

import com.athielen.boringdomainservice.controller.v1.domain.GithubDetailsResponse
import com.athielen.boringdomainservice.integrationTests.github.GithubIntegration
import com.athielen.boringdomainservice.integrationTests.github.domain.GithubRepoResponse
import com.athielen.boringdomainservice.integrationTests.github.domain.GithubUsersDetailResponse
import com.fasterxml.jackson.databind.ObjectMapper
import spock.lang.Specification;

class GithubServiceImplSpec extends Specification {

    GithubIntegration githubIntegration = Mock()

    GithubService githubService = new GithubServiceImpl(githubIntegration: githubIntegration)

    ObjectMapper objectMapper = new ObjectMapper()

    String userDetailString = """{"avatar_url":"https://avatars.githubusercontent.com/u/14352273?v=4","html_url":"https://api.github.com/users/athielen","name":"Anthony Thielen","location":"Minneapolis, MN","email":null,"created_at":"2015-09-18T18:58:14Z"}"""

    String repoDetailString = """[{"name":"adsbexchange-documentation","url":"https://api.github.com/repos/athielen/adsbexchange-documentation"},{"name":"athielen.github.io","url":"https://api.github.com/repos/athielen/athielen.github.io"},{"name":"boring-domain-service","url":"https://api.github.com/repos/athielen/boring-domain-service"},{"name":"chowdown","url":"https://api.github.com/repos/athielen/chowdown"},{"name":"dev-setup","url":"https://api.github.com/repos/athielen/dev-setup"},{"name":"dev_inspirations","url":"https://api.github.com/repos/athielen/dev_inspirations"},{"name":"hello-world","url":"https://api.github.com/repos/athielen/hello-world"},{"name":"hello-world-server","url":"https://api.github.com/repos/athielen/hello-world-server"},{"name":"line-ingress","url":"https://api.github.com/repos/athielen/line-ingress"},{"name":"shell-gists","url":"https://api.github.com/repos/athielen/shell-gists"},{"name":"size-limit","url":"https://api.github.com/repos/athielen/size-limit"},{"name":"templates","url":"https://api.github.com/repos/athielen/templates"}]"""

    String resultString = """{"avatar":"https://avatars.githubusercontent.com/u/14352273?v=4","email":null,"url":"https://api.github.com/users/athielen","repos":[{"name":"adsbexchange-documentation","url":"https://api.github.com/repos/athielen/adsbexchange-documentation"},{"name":"athielen.github.io","url":"https://api.github.com/repos/athielen/athielen.github.io"},{"name":"boring-domain-service","url":"https://api.github.com/repos/athielen/boring-domain-service"},{"name":"chowdown","url":"https://api.github.com/repos/athielen/chowdown"},{"name":"dev-setup","url":"https://api.github.com/repos/athielen/dev-setup"},{"name":"dev_inspirations","url":"https://api.github.com/repos/athielen/dev_inspirations"},{"name":"hello-world","url":"https://api.github.com/repos/athielen/hello-world"},{"name":"hello-world-server","url":"https://api.github.com/repos/athielen/hello-world-server"},{"name":"line-ingress","url":"https://api.github.com/repos/athielen/line-ingress"},{"name":"shell-gists","url":"https://api.github.com/repos/athielen/shell-gists"},{"name":"size-limit","url":"https://api.github.com/repos/athielen/size-limit"},{"name":"templates","url":"https://api.github.com/repos/athielen/templates"}],"user_name":"athielen","display_name":"Anthony Thielen","geo_location":"Minneapolis, MN","created_at":"2015-09-18 18:58:14"}"""

    GithubUsersDetailResponse userDetails = objectMapper.readValue(userDetailString, GithubUsersDetailResponse)

    GithubRepoResponse[] repoDetails = objectMapper.readValue(repoDetailString, GithubRepoResponse[])

    GithubDetailsResponse githubDetailsResponse = objectMapper.readValue(resultString, GithubDetailsResponse)

    def "Test getGithubAggregateInfo - success"() {

        when:
        GithubDetailsResponse result = githubService.getGithubAggregateInfo("athielen")

        then:
        1 * githubIntegration.getGithubDetailsByUsername("athielen") >> userDetails
        1 * githubIntegration.getGithubReposByUsername("athielen") >> repoDetails
        0 * _

        result.avatar == githubDetailsResponse.avatar
        result.createdAt == githubDetailsResponse.createdAt
        result.displayName == githubDetailsResponse.displayName
        result.email == githubDetailsResponse.email
        result.geoLocation == githubDetailsResponse.geoLocation
        result.url ==  githubDetailsResponse.url
        result.userName == githubDetailsResponse.userName

        // TODO: dnyamic testing, rather than just size check
        GithubRepoResponse[] repos = result.repos
        repos.length == 12

    }


}
