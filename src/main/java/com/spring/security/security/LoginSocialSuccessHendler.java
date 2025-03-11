package com.spring.security.security;

//classe que ira capturar os dadso que esta vindo do google

import com.spring.security.models.User;
import com.spring.security.services.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

//criando uma classe de implementação do AuthenticationSuccessHandler
//para que a authenticação qua esta vindo do google que é uma AuthenticationSuccessHandler
//se torne a nossa implementação

@Component
@RequiredArgsConstructor
public class LoginSocialSuccessHendler extends SavedRequestAwareAuthenticationSuccessHandler { //classe que implementa a interface AuthenticationSuccessHandler

    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws ServletException, IOException {

        //OAuth2AuthenticationToken é o tipo de resposta que o google envia

        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication; //peguei a minha authentication e fiz um casting para o tipo de resposta do google
        OAuth2User oAuth2User = oAuth2AuthenticationToken.getPrincipal(); //para pegar os atributos do usuario que vem do google preciso acessar o principal que retorar dados do tipo OAuth2User

        //e atraves desse oAuth2User consigo acessar todos os atributos do usuario que esta vindo do google
        //porem o que a gente precisa é do email
        String email = oAuth2User.getAttribute("email"); //acesso o atributo e informo a chave do atributo que eu quero

        User userEncontrado = userService.findByEmail(email); //busco no banco o usuario com esse email especifico

        authentication = new CustomAuthentication(userEncontrado); //e passo esse usuario para a minha classe de autenticação customizada, para se autenticar

        SecurityContextHolder.getContext().setAuthentication(authentication); //e eu set o context da minha authentication para que seja do tipo CustomAuthentication

        super.onAuthenticationSuccess(request, response, authentication); //e eu passo para a minha super classe as resposta
    }
}
