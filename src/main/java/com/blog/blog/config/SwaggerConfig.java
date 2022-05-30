package com.blog.blog.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableAutoConfiguration
@ComponentScan
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket postsApi() {
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("blog app API")
                .description("blog app REST API documentation")
                .version("1.0.0")
                .build();

        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.blog.blog"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo)
                .enable(true);
    }
}