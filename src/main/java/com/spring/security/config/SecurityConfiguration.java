package com.spring.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity //enabling security
public class SecurityConfiguration {

    //configuração padrão que o spring fornece para autenticação

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception { //pode ocorrer um exception

        return security
                .csrf(AbstractHttpConfigurer::disable) //desativando o csrf(Cross-Site Request Forgery) spring gera um token csrf e é envidado para cada requisição, assim evitando requisições indevidas
                .formLogin(configurer ->{ //agora não sera mais o login padrão do spring security
                    configurer.loginPage("/login").permitAll(); //criei um login proprio para autenticação, permito que todos tenha acesso
                })
                .httpBasic(Customizer.withDefaults()) // para fazer a autenticação atraves de um outro servidor ex(Postman)
                .authorizeHttpRequests(authorize -> { //autorizando as requisições


                    authorize.requestMatchers("/login/**").permitAll(); //permitindo que todos tenha acesso ao /login
                    authorize.anyRequest().authenticated(); //as requisições so será permitida acessar se as pessoas estiverem autenticadas
                })
                .build();
    }

    //um bean que retorna a senha codificada
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    //criando um user details service para buscar usuario no banco
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder){

        //userDetails serve para criar um usuario
        UserDetails user1 = User.builder()
                .username("Diego")
                .password(passwordEncoder.encode("123"))
                .build();

        return new InMemoryUserDetailsManager(user1); //utilizando banco em memoria
    }
}
