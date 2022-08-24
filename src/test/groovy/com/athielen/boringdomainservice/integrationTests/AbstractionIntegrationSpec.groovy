package com.athielen.boringdomainservice.integrationTests

import com.athielen.boringdomainservice.common.test.util.InteractionBuilder
import com.athielen.boringdomainservice.common.test.util.WireMockInitializer
import com.github.tomakehurst.wiremock.WireMockServer
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.junit.jupiter.api.AfterEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.context.ApplicationContext
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import spock.lang.Specification


@CompileStatic
@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = [WireMockInitializer])
@TestPropertySource(locations = [
        'classpath:application-ci.properties'])
class AbstractionIntegrationSpec extends Specification {

    static {
        System.setProperty('environment', 'ci')
        System.setProperty('spring.profiles.active', 'ci')
    }

    @Autowired
    ApplicationContext applicationContext

    @Autowired
    protected TestRestTemplate restTemplate

    @Autowired
    private WireMockServer wireMockServer

    @Value('${boring.domain.service.github.client.paths.users.uri}')
    private String githubUsersUri

    @Value('${boring.domain.service.github.client.paths.userRepos.uri}')
    private String githubReposUri

    @AfterEach
    void afterEach() {
        this.wireMockServer.resetAll()
    }

    protected static MultiValueMap<String, String> headers = new LinkedMultiValueMap<>(['Content-Type': ['application/json']])

    protected <T> ResponseEntity<T> httpGet(String path, MultiValueMap headers, Class<T> responseType) {
        return restTemplate.exchange(path, HttpMethod.GET, new HttpEntity(headers), responseType)
    }

    InteractionBuilder exceptGithubUsersUri(String username) {
        return new InteractionBuilder(wireMockServer, HttpMethod.GET, githubUsersUri, ["username": username], MediaType.APPLICATION_JSON_VALUE)
    }

    InteractionBuilder exceptGithubReposUri(String username) {
        return new InteractionBuilder(wireMockServer, HttpMethod.GET, githubReposUri, ["username": username], MediaType.APPLICATION_JSON_VALUE)
    }

}
