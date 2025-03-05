package com.spring.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity //enabling security
public class SecurityConfiguration {

    //configuração padrão que o spring fornece para autenticação

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception { //pode ocorrer um exception

        return security
                .csrf(AbstractHttpConfigurer::disable) //desativando o csrf(Cross-Site Request Forgery) spring gera um token csrf e é envidado para cada requisição, assim evitando requisições indevidas
                .formLogin(Customizer.withDefaults()) //esse é o login padrão que o spring disponibiliza para se autenticar
                .httpBasic(Customizer.withDefaults()) // para fazer a autenticação atraves de um outro servidor ex(Postman)
                .authorizeHttpRequests(authorize -> { //autorizando as requisições
                    authorize.anyRequest().authenticated(); //as requisições so será permitida acessar pessoas autenticadas
                })
                .build();
    }
}
