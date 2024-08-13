# ReadMe

## Description

A keycloak provider with kafka project example

## Keycloak Commands

<b>Keycloak Script Location for Adding a User </b></br>
-> cd /opt/jboss/keycloak/bin

<b>Keycloak Script Command for Adding a User </b></br>
-> bash add-user-keycloak.sh --user admin --password 123

## Kafka Commands

### **Script Command to Connect Kafka Container Terminal from Powershell**
-> docker exec -it kafka1 /bin/bash

### **Create Topic**

kafka-topics --bootstrap-server localhost:9092 --topic first_topic --create </br>

### **List Topics**

kafka-topics --bootstrap-server localhost:9092 --list </br>

### **Consume Topic**

kafka-console-consumer --bootstrap-server localhost:9092 --topic first_topic </br>
