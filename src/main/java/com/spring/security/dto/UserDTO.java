package com.spring.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(name = "User") //auterando o schema no swagger
public record UserDTO(
        @Schema(name = "Login") //tambem posso alterar os nome das propriedades
        String login,
        String password,
        String email,
        List<String> roles
) {
}
