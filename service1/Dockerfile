FROM openjdk:8
VOLUME /tmp

ADD /build/libs/*.jar service.jar
ENTRYPOINT ["java","-jar", "service.jar"]