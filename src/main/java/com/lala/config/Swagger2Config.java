package com.lala.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@Configuration
@EnableSwagger2
public class Swagger2Config {


    @Value("${server.port}")
    private Integer serverPort;

    @Bean
    public Docket createRestApi() {

        // 返回Docket实例
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(getApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.lala.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(getApiInfo());

    }

    private ApiInfo getApiInfo() {
        return new ApiInfoBuilder()
                .title("Restful APIs")
                .description("本文档主要适用于接口开发者")
                .termsOfServiceUrl("http://localhost:" + serverPort + "/swagger-ui.html")
                .version("1.0")
                .build();
    }
}
