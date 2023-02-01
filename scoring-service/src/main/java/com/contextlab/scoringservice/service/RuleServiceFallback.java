package com.contextlab.scoringservice.service;

import com.contextlab.scoringservice.model.Rule;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class RuleServiceFallback implements RuleServiceFeignClient{
    @Override
    public ResponseEntity<List<Rule>> getAllRules() {
        return ResponseEntity.ok(Collections.singletonList(new Rule()));
    }

    @Override
    public ResponseEntity<Rule> getRuleById(long id) {
        return ResponseEntity.ok(new Rule());
    }
}
