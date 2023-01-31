package com.contextlab.scoringservice.controller;

import com.contextlab.scoringservice.dto.ProductDto;
import com.contextlab.scoringservice.dto.RuleProductDto;
import com.contextlab.scoringservice.dto.RuleProductRequestDto;
import com.contextlab.scoringservice.model.Product;
import com.contextlab.scoringservice.model.Rule;
import com.contextlab.scoringservice.service.ProductServiceFeignClient;
import com.contextlab.scoringservice.service.RuleServiceFeignClient;
import com.contextlab.scoringservice.service.ScoringServiceImpl;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class ScoringController {

    @Autowired
    private final ScoringServiceImpl scoringService;

    @GetMapping("/scoring/all")
    public ResponseEntity<List<RuleProductDto>> scoreAllProductsByAllRules(){
        return new ResponseEntity<>(scoringService.scoreAllProducts(), HttpStatus.OK);
    }

    @GetMapping("/scoring/filter")
    public ResponseEntity<List<RuleProductDto>> scoreAllProductsByThreshold(@RequestParam Double threshold){
        return new ResponseEntity<>(scoringService.scoreAllProductsByThreshold(threshold), HttpStatus.OK);
    }

    @GetMapping("/scoring")
    public ResponseEntity<List<RuleProductDto>> scoreProductByRule(@RequestBody RuleProductRequestDto requestDto){
        return new ResponseEntity<>(scoringService.scoreProductByRule(requestDto), HttpStatus.OK);
    }
}
