FROM openjdk:8-jre-alpine
COPY ./taskcoordinator-app/build/libs/taskcoordinator-app-0.1-SNAPSHOT.jar /taskcoordinator-app-0.1-SNAPSHOT.jar
EXPOSE 8095
# Hostname is a command in a docker container that returns the container id.
# In order to enable subshell substitution, shell form is used
ENTRYPOINT exec java -jar taskcoordinator-app-0.1-SNAPSHOT.jar --spring.profiles.active=docker --host.id=$(hostname)