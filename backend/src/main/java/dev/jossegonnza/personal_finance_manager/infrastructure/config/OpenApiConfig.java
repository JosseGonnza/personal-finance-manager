package dev.jossegonnza.personal_finance_manager.infrastructure.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI personalFinanceManagerApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Personal Finance Manager API")
                        .description("API para gestionar cuentas, categorías y transacciones personales.")
                        .version("v1")
                        .contact(new Contact()
                                .name("Jose González")
                                .url("https://github.com/jossegonnza")
                                .email("accion.quevedo@gmail.com"))
                )
                .externalDocs(new ExternalDocumentation()
                        .description("Repo en GitHub")
                        .url("https://github.com/jossegonnza/personal-finance-manager"));
    }
}
