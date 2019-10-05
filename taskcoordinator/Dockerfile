FROM gradle:5.4.1 AS builder

ARG ARTIFACTORY_URL
ARG ARTIFACTORY_USER
ARG ARTIFACTORY_PASSWORD
ARG ARTIFACTORY_REPO

ENV ORG_GRADLE_PROJECT_artifactoryUrl=${ARTIFACTORY_URL}
ENV ORG_GRADLE_PROJECT_artifactoryRepo=${ARTIFACTORY_REPO}
ENV ORG_GRADLE_PROJECT_artifactoryUser=${ARTIFACTORY_USER}
# Enforce more security
ENV ORG_GRADLE_PROJECT_artifactoryPassword=${ARTIFACTORY_PASSWORD}

WORKDIR /project

COPY . /project

RUN gradle -Dorg.gradle.daemon=false -x test :taskcoordinator-app:build

FROM openjdk:8-jre-alpine
COPY --from=builder /project/taskcoordinator-app/build/libs/taskcoordinator-app-0.1-SNAPSHOT.jar /taskcoordinator-app-0.1-SNAPSHOT.jar
EXPOSE 8095
# Hostname is a command in a docker container that returns the container id.
# In order to enable subshell substitution, shell form is used
ENTRYPOINT exec java -jar taskcoordinator-app-0.1-SNAPSHOT.jar --spring.profiles.active=docker --host.id=$(hostname)