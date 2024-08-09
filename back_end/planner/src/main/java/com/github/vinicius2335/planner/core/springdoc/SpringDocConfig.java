package com.github.vinicius2335.planner.core.springdoc;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {
    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
                .info(
                        new Info()
                                .title("journey")
                                .version("1.0.0")
                                .description("Especificações da API para back-end da aplicação journey construida durante o NLW Journey da Rocketseat")
                                .license(new License()
                                        .name("Apache 2.0")
                                        .url("https://www.apache.org/licenses/LICENSE-2.0")
                                )
                );
    }
}
