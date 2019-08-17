FROM openjdk:8-jre-alpine
COPY ./ordermanagement-app/build/libs/order-management-service-app-0.1-SNAPSHOT.jar /order-management-service-app-0.1-SNAPSHOT.jar
EXPOSE 8091
ENTRYPOINT ["java", "-jar", "order-management-service-app-0.1-SNAPSHOT.jar", "--spring.profiles.active=docker"]
