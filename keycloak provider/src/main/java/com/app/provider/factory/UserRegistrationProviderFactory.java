package com.app.provider.factory;


import com.app.provider.UserRegistrationProvider;
import com.app.model.UserDetail;
import com.app.producer.AuditLogProducer;
import org.apache.kafka.clients.producer.Producer;
import org.keycloak.Config;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventListenerProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class UserRegistrationProviderFactory implements EventListenerProviderFactory {

    private static final String PROVIDER_ID = "logging-event-listener";
    private static final Logger logger = LoggerFactory.getLogger(UserRegistrationProviderFactory.class);
    private final Producer<String, UserDetail> producer = AuditLogProducer.getInstance().getProducer();

    @Override
    public EventListenerProvider create(KeycloakSession keycloakSession) {
        logger.info("********************************************************** CREATED");
        return new UserRegistrationProvider(keycloakSession, producer);
    }

    @Override
    public void init(Config.Scope scope) {

    }

    @Override
    public void postInit(KeycloakSessionFactory keycloakSessionFactory) {

    }

    @Override
    public void close() {
        producer.flush();
        producer.close();
        logger.info("********************************************************** CLOSED");
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }
}
