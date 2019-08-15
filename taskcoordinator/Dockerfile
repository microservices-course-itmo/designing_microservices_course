FROM openjdk:8-jre-alpine
COPY ./app/build/libs/taskcoordinator-app-0.1-SNAPSHOT.jar /taskcoordinator-app-0.1-SNAPSHOT.jar
EXPOSE 8095
ENTRYPOINT ["java", "-jar", "taskcoordinator-app-0.1-SNAPSHOT.jar", "--spring.profiles.active=docker"]
