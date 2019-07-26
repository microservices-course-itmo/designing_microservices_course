# Eureka Discovery service
## Overview 
Application provides service discovery mechanism - each service knows the addresses of existing Eureka instances and 
sends heartbeats to it periodically. So long as Eureka receives these heartbeats from service it is considered to be 
registered. It's easier to think about Eureka like it was just a map where the key is some service name and the value is 
the list of working instances of this service. Each service may request for the list of working instances by passing the 
service name.

## Build and run discovery service
Eureka server is represented by Spring Boot application and is built in executable jar which in turn is packed in docker 
image. Eureka server docker image with `eureka-server` tag might be created during the gradle build. In order to create 
Eureka image you should run `gradle build buildImage` command from the eureka project root directory. Eureka can be run 
as part of our infrastructure services by invoking `docker-compose up` from the project root directory.