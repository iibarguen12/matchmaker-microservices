package com.contextlab.scoringservice.dto;

import com.contextlab.scoringservice.model.Condition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RuleProductDto implements Serializable {
    private Long ruleId;
    private Set<Condition> ruleConditions;
    private double ruleScore;
    private Set<ProductDto> ruleProducts;
}
