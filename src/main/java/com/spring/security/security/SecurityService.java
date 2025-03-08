package com.spring.security.security;

import com.spring.security.models.User;
import com.spring.security.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component //classe é um component para ser gerenciado pelo spring
@RequiredArgsConstructor
public class SecurityService {

    private final UserService userService;

    //metodo que retorna o usuario logado no momento

    public User userLogado(){

        //obtem os dados do usuario autenticado no momento
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal(); //o usuario logado é um userDetails vai receber a authentication.getPrincipal() que é os dados do usuario logado
        String login = userDetails.getUsername();
        return userService.findByLogin(login);
    }
}
