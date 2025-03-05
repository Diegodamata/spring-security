package com.spring.security.services;

import com.spring.security.models.Product;
import com.spring.security.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;

    public Product createProduct(Product entity) {
        return repository.save(entity);
    }
}
