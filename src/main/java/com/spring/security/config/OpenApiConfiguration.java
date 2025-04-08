package com.spring.security.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
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
        ),
        security = { //adicionando um botão na nossa documentação no swagger para fazer a requisição via token
                @SecurityRequirement(name = "bearerAuth")
        }
)
@SecurityScheme(
        name = "bearerAuth",           //nome da configuração (vai ser referenciado em outro lugar)
        type = SecuritySchemeType.HTTP, //tipo de autenticação HTTP (poderia ser apiKey, oauth2, etc.)
        bearerFormat = "JWT",           //formato do token (informativo para o Swagger)
        scheme = "bearer",              //esquema de autenticação usado no cabeçalho (Bearer token)
        in = SecuritySchemeIn.HEADER    //local onde o token será enviado (no header da requisição)
)
public class OpenApiConfiguration {
}
