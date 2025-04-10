package com.spring.security.security;

import com.spring.security.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

//classe customizada de UserDetailsService para buscar o usuario do banco e autenticalo

@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    //essa classe buscava um user no banco e comparava os dados debaixo dos panos, se os dados baterem
    //ele cria uma authentication, porem agora vou criar uma authentication customizada, e prover os dados manualmente

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {

        com.spring.security.models.User user = userService.findByLogin(login);

        if(user == null){
            throw new UsernameNotFoundException("User not found");
        }

        return User.builder()
                .username(user.getLogin())
                .password(user.getPassword())
                .roles(user.getRoles().toArray(new String[user.getRoles().size()]))
                .build();
    }
}
