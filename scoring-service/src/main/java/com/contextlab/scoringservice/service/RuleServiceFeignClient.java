package com.contextlab.scoringservice.service;


import com.contextlab.scoringservice.model.Rule;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name="${services.rules.name}", url="${services.rules.url}")
public interface RuleServiceFeignClient {

    @GetMapping("/rules")
    public ResponseEntity<List<Rule>> getAllRules();

    @GetMapping("/rules/{id}")
    public ResponseEntity<Rule> getRuleById(@PathVariable("id") long id);
}
