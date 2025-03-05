package com.spring.security.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "produtos")
public class Produto {

    private UUID id;

    private String nome;

    private BigDecimal preco;
}
