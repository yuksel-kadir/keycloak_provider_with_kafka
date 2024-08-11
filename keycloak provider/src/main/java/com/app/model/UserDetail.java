package com.app.model;


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

    @Override
    public String toString() {
        return "UserDetail{" + "id='" + id + '\'' + ", email='" + email + '\'' + ", firstName='" + firstName + '\'' + ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' + ", eventTime='" + eventTime + '\'' + ", eventType='" + eventType + '\'' + ", eventResourceType='" +
                eventResourceType + '\'' + ", eventResourcePath='" + eventResourcePath + '\'' + '}';
    }

    public UserDetail() {

    }

    public UserDetail(String id, String email, String firstName, String lastName, String username, String eventTime, String eventType, String eventResourceType,
                      String eventResourcePath) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.eventTime = eventTime;
        this.eventType = eventType;
        this.eventResourceType = eventResourceType;
        this.eventResourcePath = eventResourcePath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventResourceType() {
        return eventResourceType;
    }

    public String getEventResourcePath() {
        return eventResourcePath;
    }

    public void setEventResourceType(String eventResourceType) {
        this.eventResourceType = eventResourceType;
    }

    public void setEventResourcePath(String eventResourcePath) {
        this.eventResourcePath = eventResourcePath;
    }
}
