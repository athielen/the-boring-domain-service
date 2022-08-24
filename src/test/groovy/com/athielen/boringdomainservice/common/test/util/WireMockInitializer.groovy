package com.athielen.boringdomainservice.common.test.util

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.event.ContextClosedEvent

class WireMockInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    void initialize(ConfigurableApplicationContext configurableApplicationContext) {
        WireMockServer wireMockServer = new WireMockServer(new WireMockConfiguration().port(8080))
        wireMockServer.start()

        configurableApplicationContext
                .getBeanFactory()
                .registerSingleton("wireMockServer", wireMockServer)

        configurableApplicationContext.addApplicationListener { applicationEvent ->
            if (applicationEvent instanceof ContextClosedEvent) {
                wireMockServer.stop()
            }
        }

        TestPropertyValues.of(Map.of("todo_base_url", "http://localhost:" + wireMockServer.port()))
                .applyTo(configurableApplicationContext)
    }
}