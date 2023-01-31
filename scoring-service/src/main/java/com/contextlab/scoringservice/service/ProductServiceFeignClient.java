package com.contextlab.scoringservice.service;

import com.contextlab.scoringservice.model.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@FeignClient(name="${services.products.name}")
public interface ProductServiceFeignClient {
    @GetMapping("api/v1/products")
    ResponseEntity<List<Product>> getAllProducts();

    @GetMapping("api/v1/products/{id}")
    ResponseEntity<Product> getProductById(@PathVariable("id") long id);

}
