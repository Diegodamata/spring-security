package com.spring.security.mapper;

import com.spring.security.dto.ProductDTO;
import com.spring.security.models.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product toEntity(ProductDTO dto);
    ProductDTO toDTO(Product product);
}
