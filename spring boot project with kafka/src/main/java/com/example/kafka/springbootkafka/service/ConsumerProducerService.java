package com.example.kafka.springbootkafka.service;

import com.example.kafka.springbootkafka.model.entity.UserDetail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConsumerProducerService {

    private final KafkaTemplate<String, Object> userDetailTemplate;

    @Value(value = "${spring.kafka.consumer.custom_consumer_2.topic}")
    private String secondTopic;

    @KafkaListener(topics = "${spring.kafka.consumer.custom_consumer_1.topic}",
            groupId = "${spring.kafka.consumer.custom_consumer_1.group-id}")
    public void firstTopicListener(UserDetail message) {
        processAndForwardMessage("spring.kafka.consumer.custom_consumer_1.topic", message, secondTopic);
    }

    @KafkaListener(topics = "${spring.kafka.consumer.custom_consumer_2.topic}",
            groupId = "${spring.kafka.consumer.custom_consumer_2.group-id}")
    public void secondTopicListener(UserDetail message) {
        log.info("Received message from topic -> spring.kafka.consumer.custom_consumer_2.topic: {}", message);
        log.info("======================================");
    }

    private void processAndForwardMessage(String sourceTopic, UserDetail message, String destinationTopic) {
        log.info("Received message from topic -> {}: {}", sourceTopic, message);
        log.info("Forwarding message to topic -> {} ...", destinationTopic);
        log.info("--------------------------------------");

        try {
            userDetailTemplate.send(destinationTopic, message);
        } catch (Exception e) {
            log.error("Failed to send message to topic -> {}: {}", destinationTopic, e.getMessage(), e);
        }
    }

}
