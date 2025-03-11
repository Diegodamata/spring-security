package com.spring.security.models;

import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Type;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String login;

    private String password;

    private String email;

    @Type(ListArrayType.class) //hypersistence serve para fazer a conversão de list para array para ser armazenado no banco
    @Column(name = "roles", columnDefinition = "varchar[]")
    private List<String> roles;
}
