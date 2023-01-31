package com.contextlab.scoringservice.controller;

import com.contextlab.scoringservice.dto.RuleProductRequestDto;
import com.contextlab.scoringservice.dto.ThresholdResultDto;
import com.contextlab.scoringservice.model.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Array;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ScoringControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Order(1)
    void scoreAllProductsByAllRules() throws Exception {
        // given - the information from the product-service and the rule-service using OpenFeign

        // when -  calculating all the products and rules
        ResultActions response = mockMvc.perform(
                get("/api/v1/scoring/all")
        );

        // then - should return ok status and the 12 records
        response.andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(12)));
    }

    @Test
    @Order(2)
    void scoreAllProductsByThreshold() throws Exception {
        // given - the information from the product-service and the rule-service using OpenFeign

        // when -  calculating all the products over and equal the threshold 90
        ResultActions response = mockMvc.perform(
                get("/api/v1/scoring/filter?threshold=90")
        );

        // then - should return ok status and 5 records
        response.andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(5)));
    }

    @Test
    @Order(3)
    void scoreProductByRule() throws Exception {
        // given - the information from the product-service and the rule-service using OpenFeign
        // and a list of products and rules
        RuleProductRequestDto requestDto =
                RuleProductRequestDto.builder()
                        .productIds(Arrays.asList(2,3))
                        .ruleIds(Arrays.asList(8))
                        .build();

        // when -  calculating all the products over and equal the threshold 90
        ResultActions response = mockMvc.perform(
                get("/api/v1/scoring/filter?threshold=90")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
        );

        // then - should return ok status and 1 rule with 2 products
        response.andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(5)));
    }

    @Test
    @Order(4)
    void getTotalsByThreshold()  throws Exception {
        // given - the information from the product-service and the rule-service using OpenFeign

        // when -  calculating the total and average price based on the threshold 90
        ResultActions response = mockMvc.perform(
                get("/api/v1/scoring/total?threshold=90")
        );

        // then - should return ok status and 5 records, and the first totalPrice have to be 93.97
        response.andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].totalPrice",Matchers.is(93.97)));
    }
}