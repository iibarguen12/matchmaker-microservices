package com.contextlab.scoringservice.service;

import com.contextlab.scoringservice.model.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@FeignClient(name="${services.products.name}", url="${services.products.url}")
public interface ProductServiceFeignClient {
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts();

    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") long id);

}
