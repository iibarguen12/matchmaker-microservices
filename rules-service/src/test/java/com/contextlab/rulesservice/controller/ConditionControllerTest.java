package com.contextlab.rulesservice.controller;

import com.contextlab.rulesservice.entity.Condition;
import com.contextlab.rulesservice.entity.Rule;
import com.contextlab.rulesservice.repository.RuleRepository;
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
class ConditionControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RuleRepository ruleRepository;


    @Test
    @Order(1)
    void getAllConditions() throws Exception {
        // given - the pre inserted 22 conditions with the import.sql file

        // when -  getting all the conditions
        ResultActions response = mockMvc.perform(
                get("/api/v1/conditions")
        );

        // then - should return ok status and the 22 records
        response.andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()",Matchers.is(22)));
    }

    @Test
    @Order(2)
    void getConditionById() throws Exception {
        // given - the pre inserted 22 conditions with the import.sql file

        // when -  getting the condition id 20
        ResultActions response = mockMvc.perform(
                get("/api/v1/conditions/20")
        );

        // then - should return ok status and the condition id 20
        response.andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.value", Matchers.is("Tools")));
    }

    @Test
    @Order(3)
    void createCondition() throws Exception {
        // given - a new rule and condition object

        // create a new rule
        Rule rule = Rule.builder().score(10).build();
        rule = ruleRepository.save(rule);

        // Create a new condition
        Set<Rule> rules = new HashSet<>();
        rules.add(rule);
        Clob valueClob = new javax.sql.rowset.serial.SerialClob("red".toCharArray());
        Condition newCondition = Condition.builder()
                .id(null)
                .attribute("color")
                .value(valueClob)
                .operator("==")
                .rules(rules).build();


        // when - sending the request to create a condition
        ResultActions response = mockMvc.perform(
                post("/api/v1/conditions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCondition))
        );

        // then - should return ok status and the data of the new condition
        response.andDo(print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.attribute", Matchers.is(newCondition.getAttribute())));
    }

    @Test
    @Order(4)
    void updateCondition() throws Exception {
        // given - the pre inserted 22 conditions with the import.sql file and a new condition object
        int quantity = 100;
        Clob valueClob = new javax.sql.rowset.serial.SerialClob(Integer.toString(quantity).toCharArray());

        Condition newCondition = Condition.builder()
                .id(null)
                .attribute("quantity")
                .value(valueClob)
                .operator(">=").build();

        // when -  modifying the condition id 20
        ResultActions response = mockMvc.perform(
                put("/api/v1/conditions/20")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCondition))
        );

        // then - should return Ok status and the modified object
        response.andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(20)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.value", Matchers.is(quantity)));
    }

    @Test
    @Order(5)
    void deleteCondition() throws Exception {
        // given - the pre inserted 22 conditions with the import.sql file

        // when -  deleting the condition id 20
        ResultActions response = mockMvc.perform(
                delete("/api/v1/conditions/20")
        );

        // then - should return deleted (no content) status
        response.andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}