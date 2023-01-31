package com.contextlab.scoringservice.model;

import com.contextlab.scoringservice.util.ClobDeserializer;
import com.contextlab.scoringservice.util.ClobSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import java.sql.Clob;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(exclude = "rules")
public class Condition{
    private Long id;
    private String attribute;
    @JsonSerialize(using = ClobSerializer.class)
    @JsonDeserialize(using = ClobDeserializer.class)
    private Clob value;
    private String operator;
    @JsonIgnore
    private Set<Rule> rules;
}
