package com.Study.Board.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new SearchTypeRequestConverter());
    }

    @Bean
    public WebClient DnfWebClient() {
        final String BASE_URL = "https://api.neople.co.kr/df";

        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(BASE_URL);
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);

        WebClient webClient = WebClient.builder()
                .uriBuilderFactory(factory)
                .build();

        return webClient;
    }
}
