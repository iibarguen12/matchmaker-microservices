package com.contextlab.scoringservice.service;

import com.contextlab.scoringservice.dto.RuleProductDto;
import com.contextlab.scoringservice.dto.RuleProductRequestDto;
import com.contextlab.scoringservice.model.Product;
import com.contextlab.scoringservice.model.Rule;

import java.util.List;

public interface ScoringService {
    List<RuleProductDto> scoreAllProducts();

    List<RuleProductDto> scoreAllProductsByThreshold(Double threshold);

    List<RuleProductDto> scoreProductByRule(RuleProductRequestDto requestDto);
}
