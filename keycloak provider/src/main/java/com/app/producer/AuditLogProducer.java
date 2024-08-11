package com.app.producer;


import com.app.config.AuditLogProducerConfig;
import com.app.model.UserDetail;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;


public class AuditLogProducer {

    private static AuditLogProducer auditLogProducer;
    private final Producer<String, UserDetail> producer;

    private AuditLogProducer() {
        producer = new KafkaProducer<>(AuditLogProducerConfig.getInstance().getProperties());
    }

    public static AuditLogProducer getInstance() {
        if (auditLogProducer == null) {
            auditLogProducer = new AuditLogProducer();
        }

        return auditLogProducer;
    }

    public Producer<String, UserDetail> getProducer() {
        return producer;
    }

}
