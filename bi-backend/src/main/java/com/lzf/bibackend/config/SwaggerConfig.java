package com.lzf.bibackend.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    /**
     * 根据@Tag 上的排序，写入x-order
     *
     * @return the global open api customizer
     */
    @Bean
    public GlobalOpenApiCustomizer orderGlobalOpenApiCustomizer() {
        return openApi -> {
        };
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI();
    }


}
