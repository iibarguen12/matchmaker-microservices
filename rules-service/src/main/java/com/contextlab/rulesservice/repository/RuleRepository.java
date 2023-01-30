package com.contextlab.rulesservice.repository;

import com.contextlab.rulesservice.entity.Rule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RuleRepository extends JpaRepository<Rule, Long> {
}
