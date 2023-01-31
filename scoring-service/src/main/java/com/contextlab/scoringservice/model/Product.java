package com.contextlab.scoringservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
    private Long id;
    private String name;
    private String type;
    private String color;
    private double cost;
    private double weight;
    private int quantity;
}