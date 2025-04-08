package com.spring.security.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;


//Customizando a minha documentação com swagger
//adicionando apenas essa classe de configuração já altera as configurações da documentação

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Minha api",
                version = "v1",
                contact = @Contact(
                        name = "Diego da Mata",
                        email = "diegodamatapontes@Hotmail.com",
                        url = "minhaapi.com"
                )
        )
)
public class OpenApiConfiguration {
}
