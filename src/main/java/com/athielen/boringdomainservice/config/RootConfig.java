package com.athielen.boringdomainservice.config;

import org.springframework.boot.actuate.metrics.web.client.MetricsRestTemplateCustomizer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan( basePackages = {"com.athielen.boringdomainservice"})
public class RootConfig {

    @Bean
    @DependsOn(value = {"metricsRestTemplateCustomizer"})
    public RestTemplateBuilder customRestTemplateBuilder(
            MetricsRestTemplateCustomizer metricsRestTemplateCustomizer) {
        return new RestTemplateBuilder()
                .defaultMessageConverters()
                .additionalCustomizers(metricsRestTemplateCustomizer)
                .defaultMessageConverters()
                .additionalMessageConverters(
                        new StringHttpMessageConverter(),
                        new MappingJackson2HttpMessageConverter());
    }

}
