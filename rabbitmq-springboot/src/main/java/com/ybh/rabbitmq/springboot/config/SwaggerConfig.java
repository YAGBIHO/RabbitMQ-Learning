package com.ybh.rabbitmq.springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket webApiConfig() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("webApi")
                .apiInfo(webApiInfo())
                .select()
                .build();
    }

    private ApiInfo webApiInfo() {
        return new ApiInfoBuilder()
                .title("RabbitMQ接口文档")
                .description("本文档描述了RabbitMQ微服务接口的定义")
                .version("1.0")
                .contact(new Contact("YAGBIHO", "https://www.yuque.com/docs/share/75869620-dc58-4bce-bead-ef37915a0470?# 《消息中间件之RabbitMQ》", "799493771@qq.com"))
                .build();
    }
}
