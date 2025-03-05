package com.spring.security.controllers;

import com.spring.security.dto.ProductDTO;
import com.spring.security.mapper.ProductMapper;
import com.spring.security.models.Product;
import com.spring.security.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    @PostMapping
    public ResponseEntity<Void> createProduct(@RequestBody ProductDTO dto){
        Product entity = productMapper.toEntity(dto);

        entity = productService.createProduct(entity);

        URI uri = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(entity.getId())
                    .toUri();

        return ResponseEntity.created(uri).build();
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getProducts(){
        List<Product> products = productService.getProducts();
        List<ProductDTO> dtos = products.stream()
                .map(product -> new ProductDTO(
                        product.getName(),
                        product.getPrice()
                )).toList();

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> findById(String id){
        Product product = productService.findById(UUID.fromString(id));
        return ResponseEntity.ok(productMapper.toDTO(product));
    }
}
