package com.effectivesoft.payment.web.docs;

import com.google.common.base.Predicates;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
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
@ComponentScan("com.effectivesoft.payment.web")
public class SwaggerConfig {

    @Value("${swagger.enabled}")
    private String swaggerEnabled;

    @Bean
    public Docket api() {
        boolean enabled = swaggerEnabled != null && Boolean.valueOf(swaggerEnabled);
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(enabled)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(Predicates.not(PathSelectors.regex("/error")))
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        String description = "Payment service";
        return new ApiInfoBuilder()
                .title("Payment service")
                .description(description)
                .version("0.1.0")
                .build();
    }
}
