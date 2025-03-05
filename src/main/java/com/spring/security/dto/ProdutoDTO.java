package com.spring.security.dto;

import java.math.BigDecimal;

public record ProdutoDTO(
        String nome,
        BigDecimal preco
) {
}
