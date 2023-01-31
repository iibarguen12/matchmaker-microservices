package com.contextlab.scoringservice.model;

import lombok.*;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(exclude = "conditions")
public class Rule {
    private Long id;
    private Set<Condition> conditions;
    private double score;
}
