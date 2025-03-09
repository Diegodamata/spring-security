package com.spring.security.controllers;

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
    @ResponseBody
    public String paginaHome(Authentication authentication){ //Authentication para pegar a pessoa que esta logada e autenticada
        return "Olá" + authentication.getName();
    }
}
