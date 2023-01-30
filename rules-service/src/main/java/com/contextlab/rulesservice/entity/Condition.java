package com.contextlab.rulesservice.entity;

import com.contextlab.rulesservice.serializers.ClobDeserializer;
import com.contextlab.rulesservice.serializers.ClobSerializer;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Clob;
import java.util.Set;

@Entity
@Table(name = "conditions")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(exclude = "rules")
public class Condition{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String attribute;

    @Column(nullable = false)
    @JsonSerialize(using = ClobSerializer.class)
    @JsonDeserialize(using = ClobDeserializer.class)
    private Clob value;

    @Column(nullable = false)
    private String operator;

    @JsonIgnore
    @ManyToMany(mappedBy = "conditions",fetch = FetchType.LAZY)
    @JsonIgnoreProperties ("conditions")
    private Set<Rule> rules;
}
