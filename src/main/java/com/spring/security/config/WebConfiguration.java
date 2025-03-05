package com.spring.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//classe de configuração do modelo model view controller

@Configuration
@EnableWebMvc //habilitando o modelo mvc informando para o spring que irei configurar manualmento o modelo MVC
public class WebConfiguration implements WebMvcConfigurer {

    //informei para o spring que irei configurar manualmente o MVC

    //metodo sobrescrito da interface WebMvcConfigurer, serve para adicionar uma nova view
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login"); //registro a nova view e o nome
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE); //serve para ter prioridade ser a primeira coisa a carregar
    }
}
