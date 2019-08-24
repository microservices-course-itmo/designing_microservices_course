FROM openjdk:8-jre-alpine
COPY ./laundrymanagement-app/build/libs/laundry-management-service-app-0.1-SNAPSHOT.jar /laundry-management-service-app-0.1-SNAPSHOT.jar
EXPOSE 8089
ENTRYPOINT ["java", "-jar", "laundry-management-service-app-0.1-SNAPSHOT.jar", "--spring.profiles.active=docker"]
