FROM openjdk:8-jre-alpine
COPY ./app/build/libs/user-management-service-0.1-SNAPSHOT.jar /user-management-service-0.1-SNAPSHOT.jar
EXPOSE 8094
ENTRYPOINT ["java", "-jar", "user-management-service-0.1-SNAPSHOT.jar", "--spring.profiles.active=docker"]
