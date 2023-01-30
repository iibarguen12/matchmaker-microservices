package com.contextlab.rulesservice.controller;

import com.contextlab.rulesservice.entity.Rule;
import com.contextlab.rulesservice.repository.RuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class RuleController {
    @Autowired
    private RuleRepository ruleRepository;

    @GetMapping("/rules")
    public ResponseEntity<List<Rule>> getAllRules(
            @RequestParam(required = false) String attribute){
        try{
            List<Rule> rules = new ArrayList<Rule>();
            if (attribute == null)
                rules.addAll(ruleRepository.findAll());
            if (rules.isEmpty())
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(rules, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/rules/{id}")
    public ResponseEntity<Rule> getRuleById(@PathVariable("id") long id) {
        Optional<Rule> ruleData = ruleRepository.findById(id);
        return ruleData.map(rule -> new ResponseEntity<>(rule, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/rules")
    public ResponseEntity<Rule> createRule(@RequestBody Rule rule) {
        try {
            Rule _rule = ruleRepository.save(rule);
            return new ResponseEntity<>(_rule, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/rules/{id}")
    public ResponseEntity<Rule> updateRule(@PathVariable("id") long id, @RequestBody Rule rule) {
        Optional<Rule> ruleData = ruleRepository.findById(id);

        if (ruleData.isPresent()) {
            Rule _rule = ruleData.get();
            _rule.setConditions(rule.getConditions());
            _rule.setScore(rule.getScore());
            return new ResponseEntity<>(ruleRepository.save(_rule), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/rules/{id}")
    public ResponseEntity<String> deleteRule(@PathVariable("id") long id) {
        try {
            ruleRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EmptyResultDataAccessException ex) {
            return new ResponseEntity<>("Rule id not exists", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error while deleting rule", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
