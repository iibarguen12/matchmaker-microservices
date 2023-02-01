package com.contextlab.gatewayservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HystrixFallbackRestController {

    @GetMapping("/auth-service/fallback")
    public ResponseEntity<String> getFallbackAuthServiceMsg() {
        return ResponseEntity.ok(standardFallbackMsg("Auth"));
    }

    @GetMapping("/products-service/fallback")
    public ResponseEntity<String> getFallbackProductServiceMsg() {
        return ResponseEntity.ok(standardFallbackMsg("Products"));
    }

    @GetMapping("/rules-service/fallback")
    public ResponseEntity<String> getFallbackRuleServiceMsg() {
        return ResponseEntity.ok(standardFallbackMsg("Rules"));
    }

    @GetMapping("/scoring-service/fallback")
    public ResponseEntity<String> getFallbackScoringServiceMsg() {
        return ResponseEntity.ok(standardFallbackMsg("Scoring"));
    }

    private String standardFallbackMsg(String serviceName){
        return String.format("No response from %s Service, but could be back shortly", serviceName);
    }
}
