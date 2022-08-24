package com.athielen.boringdomainservice.utils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

public class RestTemplateUtil {

    public static <T> T execute(
            RestTemplate restTemplate, HttpMethod httpMethod, String baseUri, String serviceUri,
            Map<String, String> queryMap, Map<String, ?> uriVariables, Map<String, String> headerMap,
            Object requestObject, Class<T> responseType) {
        String buildUri = buildUri(baseUri, serviceUri, queryMap);
        HttpHeaders headers = buildHeaders(headerMap);
        HttpEntity httpEntity = buildHttpEntity(headers, requestObject, httpMethod != HttpMethod.GET);
        ResponseEntity<T> responseEntity = restTemplate.exchange(buildUri, httpMethod, httpEntity, responseType, uriVariables);
        return responseEntity.getBody();
    }

    public static HttpHeaders buildHeaders(Map<String, String> headerMap) {
        HttpHeaders headers = new HttpHeaders();
        headerMap.forEach(headers::add);
        return headers;
    }

    public static HttpEntity<Object> buildHttpEntity(HttpHeaders headers, Object requestBody, boolean setContentType) {
        HttpHeaders httpHeaders = headers != null ? headers : new HttpHeaders();
        return requestBody != null ? new HttpEntity<>(requestBody, httpHeaders) : new HttpEntity<>(httpHeaders);
    }

    public static String buildUri(String baseUrl, String serviceUri, Map<String, String> queryParams) {
        UriComponentsBuilder ucb = UriComponentsBuilder.fromUriString(baseUrl).path(serviceUri);
        queryParams.forEach(ucb::queryParam);
        return ucb.build().toUriString();
    }

}