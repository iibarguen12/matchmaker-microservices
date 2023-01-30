package com.contextlab.rulesservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "rules")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(exclude = "conditions")
public class Rule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.MERGE})
    @JoinTable(name = "rule_condition",
            joinColumns = { @JoinColumn(name = "rule_id") },
            inverseJoinColumns = { @JoinColumn(name = "condition_id") })
    private Set<Condition> conditions;

    @Column(nullable = false)
    private double score;
}
