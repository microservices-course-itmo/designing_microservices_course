FROM openjdk:8-jre-alpine
COPY ./ordermanagement-app/build/libs/order-management-service.app.jar /order-management-service.app.jar
EXPOSE 8091
ENTRYPOINT ["java", "-jar", "order-management-service.app.jar", "--spring.profiles.active=docker"]
