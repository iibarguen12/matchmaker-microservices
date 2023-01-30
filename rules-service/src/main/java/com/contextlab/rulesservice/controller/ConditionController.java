package com.contextlab.rulesservice.controller;

import com.contextlab.rulesservice.entity.Condition;
import com.contextlab.rulesservice.repository.ConditionRepository;
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
public class ConditionController {
    @Autowired
    private ConditionRepository conditionRepository;

    @GetMapping("/conditions")
    public ResponseEntity<List<Condition>> getAllConditions(
            @RequestParam(required = false) String attribute){
        try{
            List<Condition> conditions = new ArrayList<Condition>();
            if (attribute == null)
                conditionRepository.findAll().forEach(conditions::add);
            else
                conditions.addAll(conditionRepository.findByAttributeContaining(attribute));

            if (conditions.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(conditions, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/conditions/{id}")
    public ResponseEntity<Condition> getConditionById(@PathVariable("id") long id) {
        Optional<Condition> conditionData = conditionRepository.findById(id);
        return conditionData.map(condition -> new ResponseEntity<>(condition, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/conditions")
    public ResponseEntity<Condition> createCondition(@RequestBody Condition condition) {
        try {
            Condition _condition = conditionRepository.save(condition);
            return new ResponseEntity<>(_condition, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/conditions/{id}")
    public ResponseEntity<Condition> updateCondition(@PathVariable("id") long id, @RequestBody Condition condition) {
        Optional<Condition> conditionData = conditionRepository.findById(id);

        if (conditionData.isPresent()) {
            Condition _condition = conditionData.get();
            _condition.setAttribute(condition.getAttribute());
            _condition.setValue(condition.getValue());
            _condition.setOperator(condition.getOperator());
            return new ResponseEntity<>(conditionRepository.save(_condition), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/conditions/{id}")
    public ResponseEntity<String> deleteCondition(@PathVariable("id") long id) {
        try {
            conditionRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (DataIntegrityViolationException ex) {
            return new ResponseEntity<>("Cannot delete condition due to relationship with a rule", HttpStatus.CONFLICT);
        }  catch (EmptyResultDataAccessException ex) {
            return new ResponseEntity<>("Condition id not exists", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error while deleting condition", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
