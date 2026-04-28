package com.arthwh.registroReceitas.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Isso diz ao Spring: "Se alguém pedir /js/app.js, procure em resources/static/js/app.js"
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }
}