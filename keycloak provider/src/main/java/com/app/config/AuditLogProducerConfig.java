package com.app.config;


import com.app.serializer.UserDetailSerializer;
import com.app.util.Constant;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class AuditLogProducerConfig {

    private static AuditLogProducerConfig auditLogProducerConfig;
    private static final String PROPERTIES_FILE = "application.properties";
    private final Properties properties;

    private AuditLogProducerConfig() {
        properties = new Properties();
        loadProperties();
        setProperties();
    }

    public static AuditLogProducerConfig getInstance() {
        if (auditLogProducerConfig == null) {
            auditLogProducerConfig = new AuditLogProducerConfig();
        }

        return auditLogProducerConfig;
    }

    public Properties getProperties() {
        return properties;
    }

    private void loadProperties() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            if (input == null) {
                throw new RuntimeException("Unable to find " + PROPERTIES_FILE);
            }
            properties.load(input);
        } catch (IOException ex) {
            throw new RuntimeException("Error loading properties file", ex);
        }
    }

    private void setProperties() {
        String kafkaIp = properties.getProperty(Constant.KAFKA_IP);
        String kafkaPort = properties.getProperty(Constant.KAFKA_PORT);
        String clientId = properties.getProperty(Constant.CLIENT_ID);

        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, String.join(":", kafkaIp, kafkaPort));
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, UserDetailSerializer.class);
        properties.put(ProducerConfig.CLIENT_ID_CONFIG, clientId);
    }

}
