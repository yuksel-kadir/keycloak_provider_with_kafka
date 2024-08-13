package com.example.kafka.springbootkafka.config;

import com.example.kafka.springbootkafka.model.entity.UserDetail;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Value(value = "${spring.kafka.consumer.custom_consumer_1.group-id}")
    private String consumer_1_group_id;

    @Value(value = "${spring.kafka.consumer.custom_consumer_2.group-id}")
    private String consumer_2_group_id;

    @Value(value = "${spring.kafka.consumer.custom_consumer_1.key-deserializer}")
    private String keyDeserializer_1;

    @Value(value = "${spring.kafka.consumer.custom_consumer_1.value-deserializer}")
    private String valueDeserializer_1;

    @Value(value = "${spring.kafka.consumer.custom_consumer_2.key-deserializer}")
    private String keyDeserializer_2;

    @Value(value = "${spring.kafka.consumer.custom_consumer_2.value-deserializer}")
    private String valueDeserializer_2;

    @Bean
    public ConsumerFactory<String, UserDetail> consumerFactory() {
        Map<String, Object> config = new HashMap<>();

        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, consumer_1_group_id);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializer_1);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializer_1);
        config.put(JsonDeserializer.VALUE_DEFAULT_TYPE, UserDetail.class.getName());
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        return new DefaultKafkaConsumerFactory<>(config);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UserDetail> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, UserDetail> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());

        return factory;
    }

    @Bean
    public ConsumerFactory<String, UserDetail> consumerFactory2() {
        Map<String, Object> config = new HashMap<>();

        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, consumer_2_group_id);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializer_2);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializer_2);
        config.put(JsonDeserializer.VALUE_DEFAULT_TYPE, UserDetail.class.getName());
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

        return new DefaultKafkaConsumerFactory<>(config);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UserDetail> kafkaListenerContainerFactory2() {
        ConcurrentKafkaListenerContainerFactory<String, UserDetail> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());

        return factory;
    }

}
