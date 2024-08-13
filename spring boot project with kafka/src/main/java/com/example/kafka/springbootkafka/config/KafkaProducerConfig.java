package com.example.kafka.springbootkafka.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

public class KafkaProducerConfig {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Value(value = "${spring.kafka.producer.key-serializer}")
    private String keySerializer;

    @Value(value = "${spring.kafka.producer.value-serializer}")
    private String valueSerializer;

    @Bean
    public ProducerFactory<String, Object> userDetailProducerFactory() {
        return new DefaultKafkaProducerFactory<>(userDetailProducerConfigs());
    }

    @Bean
    public Map<String, Object> userDetailProducerConfigs() {
        Map<String, Object> props = new HashMap<>();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer);

        return props;
    }

    @Bean
    public KafkaTemplate<String, Object> userDetailKafkaTemplate() {
        return new KafkaTemplate<>(userDetailProducerFactory());
    }

}
