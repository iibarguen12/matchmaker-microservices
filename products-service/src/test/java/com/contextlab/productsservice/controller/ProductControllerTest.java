package com.contextlab.productsservice.controller;

import com.contextlab.productsservice.entity.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @Order(1)
    void getAllProducts() throws Exception {
        // given - the pre inserted 30 products with the import.sql file

        // when -  getting all the products
        ResultActions response = mockMvc.perform(
                get("/api/v1/products")
        );

        // then - should return ok status and the 30 records
        response.andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()",Matchers.is(30)));
    }

    @Test
    @Order(2)
    void getProductById() throws Exception {
        // given - the pre inserted 30 products with the import.sql file

        // when -  getting the product id 20
        ResultActions response = mockMvc.perform(
                get("/api/v1/products/20")
        );

        // then - should return ok status and the product id 20
        response.andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("Product 20")));
    }

    @Test
    @Order(3)
    void createProduct() throws Exception {
        // given - a new product object
        Product newProduct = Product.builder().id(null).name("New Product").build();

        // when - action or behaviour that we are going test
        ResultActions response = mockMvc.perform(
                post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newProduct))
        );

        // then - should return ok status and the data of the new product
        response.andDo(print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is(newProduct.getName())));
    }

    @Test
    @Order(4)
    void updateProduct() throws Exception {
        // given - the pre inserted 30 products with the import.sql file and a new product object
        Product newProduct = Product.builder().id(null).name("New Product").build();

        // when -  deleting the product id 20
        ResultActions response = mockMvc.perform(
                put("/api/v1/products/20")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newProduct))
        );

        // then - should return  Ok status and the modified object
        response.andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(20)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is(newProduct.getName())));
    }

    @Test
    @Order(5)
    void deleteProduct() throws Exception {
        // given - the pre inserted 30 products with the import.sql file

        // when -  deleting the product id 20
        ResultActions response = mockMvc.perform(
                delete("/api/v1/products/20")
        );

        // then - should return deleted (no content) status
        response.andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}