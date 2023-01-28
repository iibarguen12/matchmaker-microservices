package com.contextlab.productsservice.repository;

import com.contextlab.productsservice.entity.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Long> {
    List<Product> findByNameContaining(String name);
}
