package com.app.provider;


import com.app.config.AuditLogTopicConfig;
import com.app.model.UserDetail;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.UserModel;
import org.keycloak.models.UserProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;


public class UserRegistrationProvider implements EventListenerProvider {

    private static final Logger logger = LoggerFactory.getLogger(UserRegistrationProvider.class);
    private KeycloakSession keycloakSession;
    Producer<String, UserDetail> producer;

    public UserRegistrationProvider(KeycloakSession keycloakSession, Producer<String, UserDetail> producer) {
        this.keycloakSession = keycloakSession;
        this.producer = producer;
    }

    private LocalDateTime parseMillisecondsToLocalDateTime(Long milliseconds) {
        // Convert milliseconds to an Instant
        Instant instant = Instant.ofEpochMilli(milliseconds);
        // Convert Instant to LocalDateTime in the system default time zone
        return LocalDateTime.ofInstant(instant, ZoneId.of("Europe/Istanbul"));
    }

    private UserModel getUserById(String userId) {
        UserProvider userProvider = keycloakSession.users();
        return userProvider.getUserById(keycloakSession.getContext().getRealm(), userId);
    }

    private void fillUserInformation(UserDetail userDetail, UserModel currentUser) {
        if (currentUser == null)
            return;

        userDetail.setId(currentUser.getId());
        userDetail.setEmail(currentUser.getEmail());
        userDetail.setFirstName(currentUser.getFirstName());
        userDetail.setLastName(currentUser.getLastName());
        userDetail.setUsername(currentUser.getUsername());
    }

    private void logUserEventInfo(UserDetail userDetail) {
        StringBuilder stringBuilder = new StringBuilder();

        String userInfo = stringBuilder.append("\n").append("Session User id: " + userDetail.getId() + "\n")
                .append("Session User Email: " + userDetail.getEmail() + "\n").append("Session User FN: " + userDetail.getFirstName() + "\n")
                .append("Session User LN: " + userDetail.getLastName() + "\n").append("Session User Username: " + userDetail.getUsername() + "\n")
                .append("Event Time: " + userDetail.getEventTime()).toString();

        logger.debug("-------------------------SESSION USER INFO-------------------------------");
        logger.info("User Info -> " + userInfo);
        logger.debug("-------------------------SESSION USER INFO-------------------------------");
    }

    private void sendUserDetailKafkaMessage(UserDetail userDetail) {
        logUserEventInfo(userDetail);

        String uuidMessageKey = UUID.randomUUID().toString();

        try {
            ProducerRecord<String, UserDetail> record =
                    new ProducerRecord<>(AuditLogTopicConfig.AUDIT_LOG_TOPIC, uuidMessageKey, userDetail);

            logger.warn("Trying to send kafka message to " + AuditLogTopicConfig.AUDIT_LOG_TOPIC);

            producer.send(record);
            //producer.flush();
        } catch (Exception e) {
            // TODO: Discuss later on - what needs to be done here?
        } finally {
            //producer.close();
        }
    }

    @Override
    public void onEvent(Event event) {
        logger.info("--------------------------NORMAL USER EVENT LOG START----------------------------------");
        LocalDateTime localDateTime = parseMillisecondsToLocalDateTime(event.getTime());
        logger.info("RESULT -> " + event.getDetails());
        logger.info("Event type -> " + event.getType().name());
        logger.info("Event Time -> " + localDateTime);
        logger.info("Who did it -> " + event.getUserId());
        logger.info("IP address -> " + event.getIpAddress());
        logger.info("Event Details -> " + event.getDetails());
        logger.info("--------------------------NORMAL USER EVENT LOG END----------------------------------");

        UserDetail userDetail = new UserDetail();
        userDetail.setEventTime(parseMillisecondsToLocalDateTime(event.getTime()).toString());
        userDetail.setEventType(event.getType().toString());

        UserModel currentUser = getUserById(event.getUserId());
        fillUserInformation(userDetail, currentUser);

        sendUserDetailKafkaMessage(userDetail);
    }

    @Override
    public void onEvent(AdminEvent adminEvent, boolean b) {
        if ("USER".equals(adminEvent.getResourceType().name()) && "CREATE".equals(adminEvent.getOperationType().name())) {
            logger.info("--USER-CREATE-LOG------------------------------------------------------");
            logger.info("RESULT -> " + adminEvent.getRepresentation());
            logger.info("Resource Path: " + adminEvent.getResourcePath());
            logger.info("Event resource type " + adminEvent.getResourceType().name());
            logger.info("Event operation type " + adminEvent.getOperationType().name());
            logger.info("Who did it -> " + adminEvent.getAuthDetails().getUserId());
            logger.info("--USER-CREATE-LOG------------------------------------------------------");
        } else {
            logger.info("------------------------------------------------------------");
            logger.info("RESULT: " + adminEvent.getRepresentation());
            logger.info("Resource Path: " + adminEvent.getResourcePath());
            logger.info("Event resource type " + adminEvent.getResourceType().name());
            logger.info("Event operation type " + adminEvent.getOperationType().name());
            logger.info("Who did it -> " + adminEvent.getAuthDetails().getUserId());
            logger.info("------------------------------------------------------------");
        }

        UserDetail userDetail = new UserDetail();
        userDetail.setEventTime(parseMillisecondsToLocalDateTime(adminEvent.getTime()).toString());
        userDetail.setEventType(adminEvent.getOperationType().name());
        userDetail.setEventResourceType(adminEvent.getResourceType().name());
        userDetail.setEventResourcePath(adminEvent.getResourcePath());

        UserModel currentUser = getUserById(adminEvent.getAuthDetails().getUserId());
        fillUserInformation(userDetail, currentUser);

        sendUserDetailKafkaMessage(userDetail);
    }

    @Override
    public void close() {
        producer.flush();
        producer.close();
    }
}
