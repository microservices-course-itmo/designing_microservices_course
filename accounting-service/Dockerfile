FROM openjdk:8-jre-alpine
COPY ./accounting-app/build/libs/accounting-service-app-0.1-SNAPSHOT.jar /accounting-service-app-0.1-SNAPSHOT.jar
EXPOSE 8093
ENTRYPOINT ["java", "-jar", "accounting-service-app-0.1-SNAPSHOT.jar", "--spring.profiles.active=docker"]
