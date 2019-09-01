FROM openjdk:8-jre-alpine
COPY ./usermanagement-app/build/libs/usermanagement-app-0.1-SNAPSHOT.jar /usermanagement-app-0.1-SNAPSHOT.jar
EXPOSE 8094
ENTRYPOINT ["java", "-jar", "usermanagement-app-0.1-SNAPSHOT.jar", "--spring.profiles.active=docker"]
