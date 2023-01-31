package com.contextlab.scoringservice.service;


import com.contextlab.scoringservice.model.Rule;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name="${services.rules.name}")
public interface RuleServiceFeignClient {

    @GetMapping("api/v1/rules")
    ResponseEntity<List<Rule>> getAllRules();

    @GetMapping("api/v1/rules/{id}")
    ResponseEntity<Rule> getRuleById(@PathVariable("id") long id);
}
