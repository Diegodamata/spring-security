package com.spring.security.dto;

import java.math.BigDecimal;

public record ProductDTO(
        String name,
        BigDecimal price
) {
}
