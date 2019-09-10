FROM gradle:5.4.1 AS builder

ARG ARTIFACTORY_URL
ARG ARTIFACTORY_USER
ARG ARTIFACTORY_PASSWORD

ENV ORG_GRADLE_PROJECT_artifactoryRepo=${ARTIFACTORY_URL}
ENV ORG_GRADLE_PROJECT_artifactoryUser=${ARTIFACTORY_USER}
# Enforce more security
ENV ORG_GRADLE_PROJECT_artifactoryPassword=${ARTIFACTORY_PASSWORD}

WORKDIR /project

COPY . /project

RUN gradle :laundrymanagement-app:build

FROM openjdk:8-jre-alpine
COPY --from=builder /project/laundrymanagement-app/build/libs/laundry-management-service-app-0.1-SNAPSHOT.jar /laundry-management-service-app-0.1-SNAPSHOT.jar
EXPOSE 8089
ENTRYPOINT ["java", "-jar", "laundry-management-service-app-0.1-SNAPSHOT.jar", "--spring.profiles.active=docker"]
