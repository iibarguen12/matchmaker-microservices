package com.contextlab.scoringservice.service;

import com.contextlab.scoringservice.dto.RuleProductDto;
import com.contextlab.scoringservice.dto.RuleProductRequestDto;
import com.contextlab.scoringservice.dto.ThresholdResultDto;

import java.util.List;

public interface ScoringService {
    List<RuleProductDto> scoreAllProducts();

    List<RuleProductDto> scoreAllProductsByThreshold(Double threshold);

    List<RuleProductDto> scoreProductByRule(RuleProductRequestDto requestDto);

    List<ThresholdResultDto> getTotalsByThreshold(Double threshold);
}
