package com.spring.security.controllers;

import com.spring.security.dto.ProductDTO;
import com.spring.security.mapper.ProductMapper;
import com.spring.security.models.Product;
import com.spring.security.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    @PostMapping
    @PreAuthorize("hasAnyRole('FUNCIONARIO', 'GERENTE')") //criando authorize no crontroller
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
    @PreAuthorize("hasAnyRole('FUNCIONARIO', 'GERENTE')")
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
    @PreAuthorize("hasAnyRole('FUNCIONARIO', 'GERENTE')")
    public ResponseEntity<ProductDTO> findById(@PathVariable("id") String id){
        Product product = productService.findById(UUID.fromString(id));
        return ResponseEntity.ok(productMapper.toDTO(product));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('GERENTE')")
    public ResponseEntity<ProductDTO> update(@PathVariable("id") String id, @RequestBody ProductDTO dto){
        Product update = productService.update(UUID.fromString(id), productMapper.toEntity(dto));
        return ResponseEntity.ok(productMapper.toDTO(update));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('GERENTE')")
    public ResponseEntity<Void> delete(@PathVariable("id") String id){
        productService.delete(UUID.fromString(id));
        return ResponseEntity.noContent().build();
    }
}
