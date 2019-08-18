FROM openjdk:8-jre-alpine
COPY ./build/libs/eureka-service-discovery-0.1-SNAPSHOT.jar /eureka-service-discovery-0.1-SNAPSHOT.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "eureka-service-discovery-0.1-SNAPSHOT.jar", "--spring.profiles.active=docker"]