package com.spring.security.services;

import com.spring.security.models.Product;
import com.spring.security.models.User;
import com.spring.security.repositories.ProductRepository;
import com.spring.security.security.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;
    private final SecurityService securityService;

    public Product createProduct(Product entity) {
        User user = securityService.userLogado();
        entity.setUser(user);
        return repository.save(entity);
    }

    public List<Product> getProducts(){
        return repository.findAll();
    }

    public Product findById(UUID id){
        return repository.findById(id).orElse(null);
    }

    public Product update(UUID id, Product product){
        Product findProduct = findById(id);
        updateData(findProduct, product);
        return findProduct;
    }

    private void updateData(Product findProduct, Product product) {
        if(product.getName() != null) findProduct.setName(product.getName());
        if(product.getPrice() != null) findProduct.setPrice(product.getPrice());
    }

    public void delete(UUID id){
        Product findProduct = findById(id);
        repository.delete(findProduct);
    }
}
