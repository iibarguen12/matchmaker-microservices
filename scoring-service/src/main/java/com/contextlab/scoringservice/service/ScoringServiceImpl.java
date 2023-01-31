package com.contextlab.scoringservice.service;

import com.contextlab.scoringservice.dto.ProductDto;
import com.contextlab.scoringservice.dto.RuleProductDto;
import com.contextlab.scoringservice.dto.RuleProductRequestDto;
import com.contextlab.scoringservice.dto.ThresholdResultDto;
import com.contextlab.scoringservice.model.Condition;
import com.contextlab.scoringservice.model.Product;
import com.contextlab.scoringservice.model.Rule;
import lombok.AllArgsConstructor;
import org.apache.commons.math3.util.Precision;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static com.contextlab.scoringservice.util.TypeConversionUtils.*;

@Service
@AllArgsConstructor
public class ScoringServiceImpl implements ScoringService{
    @Autowired
    private final ProductServiceFeignClient productServiceFeignClient;
    @Autowired
    private final RuleServiceFeignClient ruleServiceFeignClient;

    @Override
    public List<RuleProductDto> scoreAllProducts() {
        return scoreProducts(
                productServiceFeignClient.getAllProducts().getBody(),
                ruleServiceFeignClient.getAllRules().getBody());
    }

    @Override
    public List<RuleProductDto> scoreAllProductsByThreshold(Double threshold) {
        return scoreProducts(
                productServiceFeignClient.getAllProducts().getBody(),
                ruleServiceFeignClient.getAllRules().getBody())
                .stream()
                .map(rp -> {
                    Set<ProductDto> products = rp.getRuleProducts()
                            .stream()
                            .filter(p -> p.getScore() >= threshold)
                            .collect(Collectors.toSet());
                    rp.setRuleProducts(products);
                    return rp;
                })
                .filter(rp -> !rp.getRuleProducts().isEmpty())
                .collect(Collectors.toList());
    }

    @Override
    public List<RuleProductDto> scoreProductByRule(RuleProductRequestDto requestDto) {
        List<Rule> rules = requestDto.getRuleIds().stream()
                .map(id -> ruleServiceFeignClient.getRuleById(id).getBody())
                .collect(Collectors.toList());
        List<Product> products = requestDto.getProductIds().stream()
                .map(id -> productServiceFeignClient.getProductById(id).getBody())
                .collect(Collectors.toList());
        return scoreProducts(products, rules);
    }

    @Override
    public List<ThresholdResultDto> getTotalsByThreshold(Double threshold){
        List<RuleProductDto> ruleProducts = scoreProducts(
                productServiceFeignClient.getAllProducts().getBody(),
                ruleServiceFeignClient.getAllRules().getBody());

        return ruleProducts.stream()
                .map(ruleProductDto -> {
                    long count = ruleProductDto.getRuleProducts().stream()
                            .filter(productDto -> productDto.getScore() >= threshold)
                            .count();
                    double totalPrice = ruleProductDto.getRuleProducts().stream()
                            .filter(productDto -> productDto.getScore() >= threshold)
                            .mapToDouble(ProductDto::getCost)
                            .sum();
                    double averagePrice = count > 0 ? totalPrice / count : 0.0;

                    return new ThresholdResultDto(
                            ruleProductDto.getRuleId(),
                            count,
                            Precision.round(totalPrice, 2),
                            Precision.round(averagePrice,2));
                })
                .filter(rp -> rp.getCount() > 0)
                .collect(Collectors.toList());
    }

    public List<RuleProductDto> scoreProducts(List<Product> products, List<Rule> rules) {
        ModelMapper modelMapper = new ModelMapper();

        return rules.stream()
                .map(rule -> {
                    RuleProductDto ruleDto = modelMapper.map(rule, RuleProductDto.class);
                    Set<ProductDto> productsDtoList = products.stream()
                            .map(product -> {
                                ProductDto productDto = modelMapper.map(product, ProductDto.class);
                                productDto.setScore(calculateProductScore(product, rule));
                                return productDto;
                            })
                            .collect(Collectors.toSet());
                    ruleDto.setRuleProducts(productsDtoList);
                    return ruleDto;
                })
                .collect(Collectors.toList());
    }

    public double calculateProductScore(Product product, Rule rule) {
        if(product == null || rule == null)
            return 0;
        long matchingConditions = rule.getConditions().stream()
                .filter(condition -> matchesCondition(product, condition))
                .count();
        return (matchingConditions / rule.getConditions().size()) * rule.getScore();
    }

    private boolean matchesCondition(Product product, Condition condition) {
        try {
            return conditionMatches(product, condition);
        } catch (SQLException e) {
            throw new RuntimeException("An error occurred while checking the condition", e);
        }
    }

    private boolean conditionMatches(Product product, Condition condition) throws SQLException {
        switch (condition.getAttribute().toLowerCase()) {
            case "color":
                return compareStrings(product.getColor(), convertClobToString(condition.getValue()), condition.getOperator());
            case "cost":
                return compareNumbers(product.getCost(), convertClobToDouble(condition.getValue()), condition.getOperator());
            case "name":
                return compareStrings(product.getName(), convertClobToString(condition.getValue()), condition.getOperator());
            case "quantity":
                return compareNumbers(product.getQuantity(), convertClobToInt(condition.getValue()), condition.getOperator());
            case "type":
                return compareStrings(product.getType(), convertClobToString(condition.getValue()), condition.getOperator());
            case "weight":
                return compareNumbers(product.getWeight(), convertClobToDouble(condition.getValue()), condition.getOperator());
            default:
                return false;
        }
    }

    private boolean compareStrings(String productValue, String conditionValue, String operator) {
        switch (operator) {
            case "==":
                return productValue.equalsIgnoreCase(conditionValue);
            case "!=":
                return !productValue.equalsIgnoreCase(conditionValue);
            default:
                return false;
        }
    }

    private boolean compareNumbers(double productValue, double conditionValue, String operator) {
        switch (operator) {
            case "<":
                return productValue < conditionValue;
            case "<=":
                return productValue <= conditionValue;
            case ">":
                return productValue > conditionValue;
            case ">=":
                return productValue >= conditionValue;
            case "==":
                return productValue == conditionValue;
            case "!=":
                return productValue != conditionValue;
            default:
                return false;
        }
    }
}
