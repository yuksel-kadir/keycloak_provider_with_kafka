package com.example.kafka.springbootkafka.service;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class BasicProducerService {
    private static final String TOPIC = "some_topic";

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String message) {

        this.kafkaTemplate.send(TOPIC, message);
    }

    @Bean
    public NewTopic createTopic() {

        return new NewTopic(TOPIC, 1, (short) 1);
    }


}
