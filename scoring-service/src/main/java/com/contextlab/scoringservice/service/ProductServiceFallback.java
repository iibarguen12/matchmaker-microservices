package com.contextlab.scoringservice.service;

import com.contextlab.scoringservice.model.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
@Component
public class ProductServiceFallback implements ProductServiceFeignClient{
    @Override
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(Collections.singletonList(new Product()));
    }

    @Override
    public ResponseEntity<Product> getProductById(long id) {
        return ResponseEntity.ok(new Product());
    }
}
