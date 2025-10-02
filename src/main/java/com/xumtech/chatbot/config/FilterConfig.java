package com.xumtech.chatbot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * Configuration class to register servlet filters and define their URL patterns.
 */
@Configuration
public class FilterConfig {

    /**
     * Registers the SecurityFilter and associates it with a specific URL pattern.
     * The API key and secret are injected as method parameters
     * to ensure they are available before instantiating the filter.
     *
     * @param apiKey    The API key injected from application.properties.
     * @param apiSecret The API secret injected from application.properties.
     * @return The configured registration bean for the security filter.
     */
    @Bean
    public FilterRegistrationBean<SecurityFilter> securityFilterRegistration(
            @Value("${chatbot.api.key}") String apiKey,
            @Value("${chatbot.api.secret}") String apiSecret) {

        FilterRegistrationBean<SecurityFilter> registrationBean = new FilterRegistrationBean<>();

        // We pass the apiKey and apiSecret to the filter constructor
        registrationBean.setFilter(new SecurityFilter(apiKey, apiSecret));

        registrationBean.addUrlPatterns("/api/v1/*");
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);

        return registrationBean;
    }
}