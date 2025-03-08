package com.spring.security.models;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "produtos")
@Data
@EntityListeners(AuditingEntityListener.class) //adicionando auditoria
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private BigDecimal price;

    @CreatedDate
    private LocalDate dataCadastro;

    @LastModifiedDate
    private LocalDate dataAtualizacao;

//
//    @ManyToOne
//    @JoinColumn(name = "id_user")
//    private User user;
}
