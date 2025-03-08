package com.spring.security.controllers;

import com.spring.security.dto.UserDTO;
import com.spring.security.mapper.UserMapper;
import com.spring.security.models.User;
import com.spring.security.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper mapper;

    @PostMapping
    public ResponseEntity<UserDTO> created(@RequestBody UserDTO dto){
        User created = userService.created(mapper.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDTO(created));
    }

}
