FROM openjdk:17-oracle
VOLUME /tmp
EXPOSE 8090
ADD target/cloudService-0.0.1-SNAPSHOT.jar cloudService.jar
ENTRYPOINT ["java", "-jar", "/cloudService.jar"]