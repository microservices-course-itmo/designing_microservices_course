FROM openjdk:8-jre-alpine
COPY ./tariffmanagement-app/build/libs/tariff-management-service-app-0.1-SNAPSHOT.jar /tariff-management-service-app-0.1-SNAPSHOT.jar
EXPOSE 8092
ENTRYPOINT ["java", "-jar", "tariff-management-service-app-0.1-SNAPSHOT.jar", "--spring.profiles.active=docker"]
