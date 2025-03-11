package com.spring.security.controllers;

import com.spring.security.security.CustomAuthentication;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller //como não é uma requisição http, uso o controller que renderiza paginas web
public class LoginViewController {

    @GetMapping("/login")
    public String pageLogin(){
        return "login"; //vai retornar o nome do arquivo sem a extensão .html
    }


    @GetMapping("/")
    @ResponseBody //agora a authentication que esta vindo google, é uma instancia de CustomAuthentication
    public String paginaHome(Authentication authentication){ //Authentication para pegar a pessoa que esta logada e autenticada
        if(authentication instanceof CustomAuthentication customAuth){
            System.out.println(customAuth.getName());
        }
        return "Olá" + authentication.getName();
    }
}
