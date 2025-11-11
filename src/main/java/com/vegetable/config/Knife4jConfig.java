package com.vegetable.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Knife4j(Swagger) API文档配置
 *
 * @author vegetable-system
 * @since 2025-11-11
 */
@Configuration
public class Knife4jConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("自动化蔬菜销售管理系统API")
                        .version("1.0.0")
                        .description("基于HarmonyOS的自动化蔬菜销售管理系统后端接口文档")
                        .contact(new Contact()
                                .name("蔬菜销售系统团队")
                                .email("support@vegetable-system.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")));
    }
}
