package com.spring.security.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.web.SecurityFilterChain;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.UUID;

@Configuration
@EnableWebSecurity
public class AuthorizationServerConfiguration {

    //implementando um novo securityFilterChain para capturar as requisições antes de serem executadas

    //criamos novamente um securityfilterchain, porem com outro nome pois já criamos um securityfilterchain

    @Bean
    @Order(1) //para definir a order que o filterchain sera chamado, dentro da cadeia de filtros ele ficara em primeiro
    public SecurityFilterChain authServerSecurityFilterChain(HttpSecurity http) throws Exception{

       // OAuth2AuthorizationServerConfigurer, que é responsável por configurar o Authorization Server.
        // Ele gerencia emissão de tokens, autenticação e autorização de clientes.
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
                OAuth2AuthorizationServerConfigurer.authorizationServer();

        http
                .securityMatcher(authorizationServerConfigurer.getEndpointsMatcher()) //estamos dizendo que esta configuração só será aplicada aos endpoints do Authorization Server (como /oauth2/token, /oauth2/authorize, /logout, etc.).
                .with(authorizationServerConfigurer, (authorizationServer) ->
                        authorizationServer
                                .oidc(Customizer.withDefaults())	//  Ativa suporte a OpenID Connect (OIDC), que é uma extensão do OAuth2 para autenticação de usuários.
                )
                .oauth2ResourceServer(
                        oauth2Rs -> oauth2Rs.jwt(Customizer.withDefaults())) //Isso define que a API aceitará apenas tokens JWT e os validará automaticamente.
                .formLogin(
                        configurer ->  configurer.loginPage("/login"));

        return http.build();
    }

    //um bean que retorna a senha codificada
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }



    //um bean que serve para formatar o tipo de token que o autentication server ira gerar
    @Bean
    public TokenSettings tokenSettings(){
        return TokenSettings.builder()
                .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED) //O OAuth2TokenFormat.SELF_CONTAINED no Spring Authorization Server define que os tokens de acesso serão autocontidos,
                // ou seja, tokens JWT (JSON Web Tokens). Isso significa que toda a informação necessária para validação está dentro do próprio token, sem precisar consultar um banco de dados ou cache externo.
                .accessTokenTimeToLive(Duration.ofMinutes(60)) //informando o tempo de duração desse token na sessao
                .build();
    }

    //para trabalhar o com tela de concentimento
    //aquela tela de concentimento que nem do google falando se vc permite que tenha acesso ao seus dados
    @Bean
    public ClientSettings clientSettings(){
        return ClientSettings.builder()
                .requireAuthorizationConsent(false) //colocando a tela de concentimento como falso, pois não vamos trabalhar com a ttela de concentimento
                .build();
    }

    // metodo para gerar o JWK, uma chave criptografica assincrona para usar em autenticação ou assinatura digital
    // JWK - JSON Web Key
    @Bean
    public JWKSource<SecurityContext> jwkSource() throws Exception { //JWKSource é uma fonde de chaves criptografadas
        RSAKey rsaKey = gerarChaveRSA(); //chave RSA é usada pra criptografia assimetrica, chama um metodo para gerar a chave RSA
        JWKSet jwkSet = new JWKSet(rsaKey); //aqui eu crio um conjunto de chaves JWK contendo a chave RSA gerada
        return new ImmutableJWKSet<>(jwkSet); //retorno um JWKSource imutavel
    }

    //metodo para gerar uma chave RSA
    private RSAKey gerarChaveRSA() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA"); //KeyPairGenerator serve para criar uma chave atraves do algoritimo RSA, chamo o metodo getInstance para iso
        keyPairGenerator.initialize(2048); //Configura o gerador para criar chaves RSA de 2048 bits, garantindo segurança.
        KeyPair keyPair = keyPairGenerator.generateKeyPair(); //chamo o KayPair atraves do keyPairGenerator.generateKeyPair() para gerar o o par de chaves RSA (privada e publica)

        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic(); //pego a chave publica atraves do KayPair
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate(); //pego a chave privada

        return new RSAKey //e preciso retornar um Objeto do tipo RSAKey
                .Builder(publicKey) //passo para o Builder a minha chave publica
                .privateKey(privateKey) //passo a minha chave privada
                .keyID(UUID.randomUUID().toString()) //e crio um id como um identificador unico dessa chave
                .build();
    }

    //um bean do JwtDecoder responsavel por validar e decodificar o meu token JWT
    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource){ //passo para o JwtDecoder onde esta a minha fonte de chaves
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource); //Usa a classe OAuth2AuthorizationServerConfiguration para criar um JwtDecoder baseado no jwkSource.
        // Esse decoder será usado para validar tokens JWT, verificando sua assinatura com a chave RSA.
    }
}
