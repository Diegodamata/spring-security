package com.spring.security.controllers;

import com.spring.security.dto.UserDTO;
import com.spring.security.mapper.UserMapper;
import com.spring.security.models.User;
import com.spring.security.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
@Tag(name = "Users") //para modificar o nome das openações no swagger
public class UserController {

    private final UserService userService;
    private final UserMapper mapper;

    @PostMapping
    @Operation(summary = "Salvar", description = "Cadastrar usuarios") //definindo a operação desse metodo no swagger
    @ApiResponses({ //uma lista de apiResponse, pois nesse metodos pode haver mais de um tipo de retorno, 201 de sucesso, dados invalidos etc...
            @ApiResponse(responseCode = "201", description = "Cadastrado com Sucesso") //aqui eu defino os possiveis retornos desse metodo
    })
    public ResponseEntity<UserDTO> created(@RequestBody UserDTO dto){
        User created = userService.created(mapper.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDTO(created));
    }

}
