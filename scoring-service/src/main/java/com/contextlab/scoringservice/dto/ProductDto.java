package com.contextlab.scoringservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto{
    private double score;
    private Long id;
    private String name;
    private String type;
    private String color;
    private double cost;
    private double weight;
    private int quantity;
}
