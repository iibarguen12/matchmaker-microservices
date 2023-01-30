package com.contextlab.rulesservice.controller;

import com.contextlab.rulesservice.entity.Condition;
import com.contextlab.rulesservice.entity.Rule;
import com.contextlab.rulesservice.repository.ConditionRepository;
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

import java.sql.Clob;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RuleControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ConditionRepository conditionRepository;

    @Test
    @Order(1)
    void getAllRules() throws Exception {
        // given - the pre inserted 12 rules with the import.sql file

        // when -  getting all the conditions
        ResultActions response = mockMvc.perform(
                get("/api/v1/rules")
        );

        // then - should return ok status and the 12 records
        response.andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(12)));
    }

    @Test
    @Order(2)
    void getRuleById() throws Exception {
        // given - the pre inserted 12 rules with the import.sql file

        // when -  getting all the conditions
        ResultActions response = mockMvc.perform(
                get("/api/v1/rules/1")
        );

        // then - should return ok status and the 12 records
        response.andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.score", Matchers.is(10.0)));
    }

    @Test
    @Order(3)
    void createRule() throws Exception {
        // given - a new rule and condition object

        // create a new condition
        Clob valueClob = new javax.sql.rowset.serial.SerialClob("red".toCharArray());
        Condition condition = Condition.builder()
                .id(null)
                .attribute("color")
                .value(valueClob)
                .operator("==")
                .build();

        condition = conditionRepository.save(condition);
        Set<Condition> conditions = new HashSet<>();
        conditions.add(condition);

        // create a new rule
        Rule rule = Rule.builder()
                .conditions(conditions)
                .score(10).build();

        // when - sending the request to create a rule
        ResultActions response = mockMvc.perform(
                post("/api/v1/rules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rule))
        );

        // then - should return ok status and the data of the new rule
        response.andDo(print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.score", Matchers.is(rule.getScore())));
    }

    @Test
    @Order(4)
    void updateRule() throws Exception {
        // given - the pre inserted 12 rules with the import.sql file and a new rule and condition objects

        // create a new condition
        int quantity = 100;
        Clob valueClob = new javax.sql.rowset.serial.SerialClob(Integer.toString(quantity).toCharArray());

        Condition condition = Condition.builder()
                .id(null)
                .attribute("quantity")
                .value(valueClob)
                .operator(">=").build();

        condition = conditionRepository.save(condition);
        Set<Condition> conditions = new HashSet<>();
        conditions.add(condition);

        // create a new rule
        Rule rule = Rule.builder()
                .id(12L)
                .conditions(conditions)
                .score(60).build();

        // when -  modifying the rule id 12
        ResultActions response = mockMvc.perform(
                put("/api/v1/rules/12")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rule))
        );

        // then - should return Ok status and the modified object
        response.andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(12)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.score", Matchers.is(60.0)));
    }

    @Test
    @Order(5)
    void deleteRule() throws Exception {
        // given - the pre inserted 12 rules with the import.sql file

        // when -  deleting the rule id 10
        ResultActions response = mockMvc.perform(
                delete("/api/v1/rules/10")
        );

        // then - should return deleted (no content) status
        response.andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}