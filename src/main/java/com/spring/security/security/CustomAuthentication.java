package com.spring.security.security;

import com.spring.security.models.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.stream.Collectors;

//classe customizada para autenticação
//para informar como eu quero retornar uma authentication
//

@RequiredArgsConstructor
@Getter
public class CustomAuthentication implements Authentication {

    private final User user; //pegando o user pois é com base nele que vou fazer as buscas

    //esse metodo retorna as authorities do user (as roles)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.user
                .getRoles()
                .stream()
                .map(SimpleGrantedAuthority::new) //como o user tem uma lista de string preciso tranformar em uma lista de grantedAuthority, SimpleGrantedAuthority é uma classe que implementa GrantedAuthority
                .collect(Collectors.toList());
    }

    //getCredentials é a senha do usuario então não é preciso retornar a senha
    @Override
    public Object getCredentials() {
        return null;
    }

    //todos os detalhes do usuario, podendo retornar qualquer coisa do usuario
    @Override
    public Object getDetails() {
        return user;
    }

    //pega os dados principal do usuario
    @Override
    public Object getPrincipal() {
        return user;
    }

    //verifica se esse usuario esta altenticado
    @Override
    public boolean isAuthenticated() {
        return true; //preciso deixar true se não nao vou conseguir logar
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
    }

    //para obter o login do usuario
    @Override
    public String getName() {
        return user.getLogin();
    }
}
