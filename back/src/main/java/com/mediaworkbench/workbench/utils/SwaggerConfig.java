package com.mediaworkbench.workbench.utils;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("spring")
                .packagesToScan("com.mediaworkbench.workbench.controller") // Updated package path
                .build();
    }
}
