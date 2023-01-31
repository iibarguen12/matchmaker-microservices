package com.contextlab.scoringservice.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import javax.sql.rowset.serial.SerialClob;
import java.io.IOException;
import java.sql.Clob;
import java.sql.SQLException;

public class ClobDeserializer extends JsonDeserializer<Clob> {

    @Override
    public Clob deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        try {
            String value = p.getValueAsString();
            return new SerialClob(value.toCharArray());
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }
}




