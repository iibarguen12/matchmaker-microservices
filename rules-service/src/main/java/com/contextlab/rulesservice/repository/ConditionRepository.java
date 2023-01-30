package com.contextlab.rulesservice.repository;

import com.contextlab.rulesservice.entity.Condition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ConditionRepository extends JpaRepository<Condition,Long> {
    List<Condition> findByAttributeContaining(String attribute);
}
