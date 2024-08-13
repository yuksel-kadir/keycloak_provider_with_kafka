package com.example.kafka.springbootkafka;

import com.example.kafka.springbootkafka.service.BasicProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private final BasicProducerService producer;

    @Autowired
    public TestController(BasicProducerService basicProducerService) {
        this.producer = basicProducerService;
    }

    @PostMapping("/publish")
    public void messageToTopic(@RequestParam("message") String message) {

        this.producer.sendMessage(message);
    }
}
