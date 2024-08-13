package com.example.kafka.springbootkafka.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDetail {

    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String username;
    private String eventTime;
    private String eventType;
    private String eventResourceType;
    private String eventResourcePath;

}
