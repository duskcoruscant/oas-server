package com.hwenbin.server.core.config;

import com.hwenbin.server.core.constant.ProjectConstant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger2 在线API文档 http://springfox.github.io/springfox/docs/current/#getting-started
 *
 * @author hwb
 * @create 2022-03-14
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

    @Bean
    public Docket buildDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(this.buildApiInfo())
                .select()
                // 扫描 controller 包
                .apis(RequestHandlerSelectors.basePackage(ProjectConstant.CONTROLLER_PACKAGE))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo buildApiInfo() {
        final Contact contact = new Contact("hwenbin", "https://github.com/duskcoruscant", "hwenbinjobs@gmail.com");

        return new ApiInfoBuilder()
                .title("APIs doc")
                .description("RESTFul APIs")
                .contact(contact)
                .version("1.0")
                .build();
    }

}
