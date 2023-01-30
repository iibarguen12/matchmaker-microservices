package com.contextlab.rulesservice.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.sql.Clob;
import java.sql.SQLException;

public class ClobSerializer extends JsonSerializer<Clob> {
    @Override
    public void serialize(Clob value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        try {
            String content = value.getSubString(1, (int) value.length());
            if (isBoolean(content)) {
                jsonGenerator.writeBoolean(Boolean.parseBoolean(content));
            } else if (isInteger(content)) {
                jsonGenerator.writeNumber(Integer.parseInt(content));
            } else if (isDouble(content)) {
                jsonGenerator.writeNumber(Double.parseDouble(content));
            } else {
                jsonGenerator.writeString(content);
            }
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    private boolean isBoolean(String content) {
        return "TRUE".equalsIgnoreCase(content) || "FALSE".equalsIgnoreCase(content);
    }

    private boolean isInteger(String content) {
        try {
            Integer.parseInt(content);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isDouble(String content) {
        try {
            Double.parseDouble(content);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

