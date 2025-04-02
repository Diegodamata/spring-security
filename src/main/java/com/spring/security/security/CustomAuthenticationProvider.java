package com.spring.security.security;

import com.spring.security.models.User;
import com.spring.security.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


//o spring por baixo dos panos fazia com que atraves do login do user ele comparava a senha e o login para fazer a authentication
//porem agora criarei uma classe de implementação do authenticationProvider
//para comparar os dados manualmente e prover uma authenticação

@Component //preciso colocar o component para infromar para o spring que sera essa classe a responsavel por criar um provedor de authentication
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;
    private final PasswordEncoder encoder;

    //metodo sobrescrito do AuthenticationProvider, para prover uma implementação de como vc quer que seja feita a autenticação
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        //login e senha que o usuario informou na tela de login, preciso comparar se batem os dados que existem no banco
        String login = authentication.getName();
        String senhaDigitada = authentication.getCredentials().toString();

        User usuarioEncontrado = userService.findByLogin(login);

        if(usuarioEncontrado == null){
            throw getUsuarioNaoEncontrado();
        }

        String senhaCodificada = usuarioEncontrado.getPassword();

        boolean senhaBatem = encoder.matches(senhaDigitada, senhaCodificada);

        if(senhaBatem){
           return new CustomAuthentication(usuarioEncontrado); //para o usuario encontrado para a minha classe de implementação para esse usuario ser autenticado
        }

        throw getUsuarioNaoEncontrado();
    }

    private UsernameNotFoundException getUsuarioNaoEncontrado() {
        return new UsernameNotFoundException("Usuario e/ou senha incorretos");
    }

    //quando digitado o login e senha o spring security criara um objeto com esse login e senh
    //e perguntara para o AuthenticationProvider se o Provider suporta esse tipo de authenticação
    //e nesse metodo eu informa qual a o tipo de classe que ele suporta

    //metodo sobrescrito para informar quais tipo de autentication ele suporta
    //vou utilizar um tipo especifico quando o usuario informa login e senha
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class); //informo que suporta uma classe de login e senha
        //essa classe tambem é uma implementação de Authentication, e é essa classe que sera passada no authenticate
    }
}

