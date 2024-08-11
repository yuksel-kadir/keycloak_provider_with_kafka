package com.app.serializer;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.app.model.UserDetail;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;


public class UserDetailSerializer implements Serializer<UserDetail> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // Nothing to configure
    }

    @Override
    public byte[] serialize(String topic, UserDetail data) {
        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new RuntimeException("Error serializing UserDetail", e);
        }
    }

    @Override
    public void close() {
        // Nothing to do
    }
}
