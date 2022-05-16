FROM openjdk:11
ADD build/libs/messenger-api.jar messenger-api.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/messenger-api.jar"]