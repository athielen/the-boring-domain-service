package com.athielen.boringdomainservice.common.test.util

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder
import com.github.tomakehurst.wiremock.matching.StringValuePattern
import groovy.util.logging.Slf4j
import org.apache.commons.lang3.StringUtils
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.util.DefaultUriBuilderFactory
import org.springframework.web.util.UriBuilder

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson
import static com.github.tomakehurst.wiremock.client.WireMock.equalToXml
import static com.github.tomakehurst.wiremock.client.WireMock.request
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo

@Slf4j
class InteractionBuilder {

    private WireMockServer wireMockServer

    private Integer count = 1
    private String uriProperty
    private Map<String, String> pathParameters = [:]
    private HttpMethod httpMethod
    private String requestBody
    private HttpStatus responseStatus = HttpStatus.OK
    private String responseBody = ""
    private String contentType
    private ignoreArrayOrder = true
    private ignoreExtraElements = false
    private MultiValueMap<String, String> requestParameters = new LinkedMultiValueMap<>()

    InteractionBuilder(WireMockServer wireMockServer, HttpMethod httpMethod, String uriProperty,
                       Map<String, String> pathParameters) {
        this.wireMockServer = wireMockServer
        this.httpMethod = httpMethod
        this.uriProperty = uriProperty
        this.pathParameters = pathParameters
    }

    InteractionBuilder(WireMockServer wireMockServer, HttpMethod httpMethod, String uriProperty,
                       Map<String, String> pathParameters, String contentType) {
        this.wireMockServer = wireMockServer
        this.httpMethod = httpMethod
        this.uriProperty = uriProperty
        this.pathParameters = pathParameters
        this.contentType = contentType
    }

    InteractionBuilder(WireMockServer wireMockServer, HttpMethod httpMethod, String uriProperty,
                       Map<String, String> pathParameters, MultiValueMap<String, String> requestParameters, String contentType) {
        this.wireMockServer = wireMockServer
        this.httpMethod = httpMethod
        this.uriProperty = uriProperty
        this.pathParameters = pathParameters
        this.contentType = contentType
        this.requestParameters = requestParameters
    }

    InteractionBuilder withRequestBody(String requestBody) {
        this.requestBody = requestBody?.trim()
        return this
    }

    InteractionBuilder thisManyTimes(Integer count) {
        this.count = count
        return this
    }

    InteractionBuilder withResponseStatus(HttpStatus responseStatus) {
        this.responseStatus = responseStatus
        return this
    }

    InteractionBuilder withResponseStatus(int responseStatus) {
        this.responseStatus = HttpStatus.resolve(responseStatus)
        return this
    }

    InteractionBuilder withResponseStatus(String responseStatus) {
        this.responseStatus = HttpStatus.resolve(Integer.parseInt(responseStatus))
        return this
    }

    InteractionBuilder withResponseBody(String responseBody) {
        this.responseBody = responseBody
        return this
    }

    InteractionBuilder with

    void build(String scenario = null, String currentScenario = null, String nextScenario = null) {

        // Prepare Mapping Builder with modified Uri if there are any path parameters
        String modifiedUriProperty = buildModifyUri()
        MappingBuilder mappingBuilder = request(httpMethod.toString(), urlEqualTo(modifiedUriProperty))

        if (StringUtils.isBlank(responseBody)) {
            log.warn("Response body is null or blank.")
        }

        // Build ResponseDefinition
        ResponseDefinitionBuilder responseDefinitionBuilder = aResponse()
                .withHeader("Content-Type", contentType)
                .withBody(responseBody)
                .withStatus(responseStatus.value())

        // Build StringValuePattern for Request Body Matching
        if (requestBody) {
            StringValuePattern stringValuePattern
            switch (contentType) {
                case (MediaType.APPLICATION_XML_VALUE):
                    stringValuePattern = equalToXml(requestBody, true)
                    break
                case (MediaType.APPLICATION_JSON_VALUE):
                    stringValuePattern = equalToJson(requestBody, ignoreArrayOrder, ignoreExtraElements)
                    break
                case (MediaType.TEXT_PLAIN_VALUE):
                    stringValuePattern = equalTo(requestBody)
                    break
                default:
                    stringValuePattern = equalToJson(requestBody, ignoreArrayOrder, ignoreExtraElements)
                    break
            }
            mappingBuilder.withRequestBody(stringValuePattern)
        } else {
            log.warn("Request body is null.")
        }

        // Build the MappingBuilder for the Stub
        mappingBuilder.willReturn(responseDefinitionBuilder)

        if (scenario && currentScenario && nextScenario) {
            this.wireMockServer.stubFor(mappingBuilder.inScenario(scenario).whenScenarioStateIs(currentScenario).willSetStateTo(nextScenario))
        } else {
            this.wireMockServer.stubFor(mappingBuilder)
        }
    }

    private String buildModifyUri() {
        if (pathParameters?.isEmpty() && requestParameters?.isEmpty()) {
            return uriProperty
        }

        UriBuilder builder = new DefaultUriBuilderFactory().builder().path(uriProperty)

        if(!requestParameters.isEmpty()) {
            builder.queryParams(requestParameters)
        }

        URI uriBuilt = pathParameters.isEmpty() ? builder.build() : builder.build(pathParameters)
        return uriBuilt.toASCIIString()
    }

}