package com.spring.security.services;

import com.spring.security.models.User;
import com.spring.security.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder; //injeto o password enconder para codificar a senha

    public User created(User user){
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User findByLogin(String login){
        return userRepository.findByLogin(login);
    }
}
