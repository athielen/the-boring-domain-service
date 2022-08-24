package com.athielen.boringdomainservice.common.test.util

import org.springframework.http.HttpStatus

class InteractionRequest {
    Integer count
    String baseUriProperty
    String uriProperty
    Map<String, String> uriParams = [:]
    String httpMethod
    String requestBody
    HttpStatus responseStatus
    String responseBody
}
