package com.spring.security.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller //como não é uma requisição http, uso o controller que renderiza paginas web
public class LoginViewController {

    @GetMapping("/login")
    public String pageLogin(){
        return "login"; //vai retornar o nome do arquivo sem a extensão .html
    }
}
