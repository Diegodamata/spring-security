package com.spring.security.config;

import com.spring.security.security.CustomUserDetailsService;
import com.spring.security.security.JwtCustomAuthenticationFilter;
import com.spring.security.security.LoginSocialSuccessHendler;
import com.spring.security.services.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity //enabling security
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true) //habilitando o metho securty para que eu coloque as authorize dentro do controller
public class SecurityConfiguration {

    //configuração padrão que o spring fornece para autenticação

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security,
                                                   LoginSocialSuccessHendler successHendler,
                                                   JwtCustomAuthenticationFilter jwtCustomAuthenticationFilter) throws Exception { //pode ocorrer um exception

        return security
                .csrf(AbstractHttpConfigurer::disable) //desativando o csrf(Cross-Site Request Forgery) spring gera um token csrf e é envidado para cada requisição, assim evitando requisições indevidas
                .formLogin(configurer ->{ //agora não sera mais o login padrão do spring security
                    configurer.loginPage("/login"); //criei um login proprio para autenticação, permito que todos tenha acesso
                })
//                .formLogin(Customizer.withDefaults()) //login padrão do security ja add automaticamente o oauth2
                .httpBasic(Customizer.withDefaults()) // para fazer a autenticação atraves de um outro servidor ex(Postman)
                .authorizeHttpRequests(authorize -> { //autorizando as requisições


                    authorize.requestMatchers("/login/**").permitAll(); //permitindo que todos tenha acesso ao /login
                    authorize.requestMatchers(HttpMethod.POST, "/users/**").permitAll();
                    authorize.anyRequest().authenticated(); //as requisições so será permitida acessar se as pessoas estiverem autenticadas
                })
//                .oauth2Login(Customizer.withDefaults()) //adicionando o oauth2 um liink do google para authentication
                .oauth2Login(oauth -> { //criando manualmente uma classe que renderiza o sucesso de altenticação com o google
                    oauth
                            .loginPage("/login") //tanto formLogin como oauth loginPage precisa esta apontando para a mesma pagina
                            .successHandler(successHendler); //quando fizer a autenticação com sucesso com o google, ira chamar a classe que passei no metodo
                })
                .oauth2ResourceServer(oauth2RS ->
                        oauth2RS.jwt(Customizer.withDefaults()))     //informando que a minha aplicação só aceitara token jwt para autenticação
                .addFilterAfter(jwtCustomAuthenticationFilter, BearerTokenAuthenticationFilter.class)
                .build();
    }


    //Configurando a segurança na web, para desabilitar o swagger
    //todas as urls adicionada aqui serão iginorados no filtro de segurança não ira ne passar
    //diferente do pormiteAll que ainda assim passa pelo filtro
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return web -> { web.ignoring().requestMatchers(
                    "/v2/api-docs/**",
                    "/v3/api-docs/**",
                    "/swagger-resources/**",
                    "/swagger-ui.html",
                    "/swagger-ui/**",
                    "/webjars/**"
            );
        };
    }

    //criando um user details service para buscar usuario no banco

    //estou utilizando esse bean para o UserService para o contexto para buscar um usuario do banco
    //porem dessa forma não configo fazer uma implementação com o google pois o google não retorna um tipo de user
    //então preciso criar uma Authentication customizada com o meu user para altenticar com base no usuario
//    @Bean
    public UserDetailsService userDetailsService(UserService userService){

        return new CustomUserDetailsService(userService); //utilizando UserDetailsService customizado, recebendo uma dependencia de service
    }



    //o spring no authenication para o roles por padrão é configurado
    //para colocar prefixo nos roles por exemplo: "ROLE_GERENTE"
    //podendo gerar um erro de não autorizado
    //para eliminar esse prefixo
    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults(){
        return new GrantedAuthorityDefaults(""); //deixo uma string vazia assim elimina o prefixo
    }

    //os token ele tembem por padrão tem um prefixo que é o SCOPE_
    //igual o bean de cima com o ROLE_ para poder eliminar o prefixc
    //preciso criar esse bean, para converter o token remomento o prefixo
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter(){
        var authoritiesConverter = new JwtGrantedAuthoritiesConverter(); //crio uma instancia de JwtGrantedAuthoritiesConverter onde contem o metodo para auterar o prefixo
        authoritiesConverter.setAuthorityPrefix(""); //acesso o metodo setAuthorityPrefix e deixo uma string vazia para eliminar o prefixo SCOPE_

        var converter = new JwtAuthenticationConverter(); //crio uma instancia de JwtAuthenticationConverter pois o meu bean retorna um objeto desse tipo
        converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter); //chamo o metodo setJwtGrantedAuthoritiesConverterpara definir que o tipo de converção será o que a gente definiu acima

        return converter;
    }
}
