# Telemetry
## 1. config-client (Device Simulator)

This application simulates a device sending out telemetry data through a REST Endpoint.
It makes use of Spring Boot Actuator to refresh configuration without having to reload/rebuild the application.

## 2. config-server (Externalized Configuration Service)

This is a configuration microservice which makes use of git updates to register changes in properties.

## 3. batch-app (Batch Job Application)

This is a batch job application comprising of scheduled jobs which consume the telemetry data from config-client and writes them to JSON files. Another job parses thes JSON files/logs and generates reports for cases where temperature exceeds 45.
