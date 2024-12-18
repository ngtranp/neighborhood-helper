## How to build/start:
1. In terminal, go to the folder
2. start Docker
3. In terminal, run "docker compose build"
4. Then, run "docker compose up"
5. Wait until everything is up

## How to test:
Everything has been provided in the Postman collection.
Only one note is that user roles can be "requester" or "helper".

## Project overview:
There are 3 micro-services: user-service, core-service and notification service. Users will only need to interact with user-service, and it will call core-service via REST call. The communication between core-service and notification-service is asynchronous and uses RabbitMQ.

user-service's APIs:
- Register user
- Login User
- Get User By Username
- Get Service request status
- Get all service requests
- Create service request
- Accept service request
- Complete service request

core-service's API (These will be called by user-service):
- Get Service request status
- Get all service requests
- Create service request
- Accept service request
- Complete service request

Message Queues:
- requestCreatedQueue
- requestCompletedQueue
- requestAcceptedQueue

## User stories:
- Users can check service request status
- Users can list all service requests
- Only 'requester' can create a service request, then a creation notification will be sent
- Only 'helper' can accept a service request, then an accept notification will be sent
- Only 'helper' can complete a service request, then a completion notification will be sent

## Diagram:
USERS --(HTTP requests)--> user-service  
user-service --(HTTP requests)--> core-service  
core-service --(RabbitMQ)--> Notification-service

## Tech stacks
Java 21
Maven
Docker
RabbitMQ
Spring Boot
Spring JPA
Spring Web
H2
Lombok
