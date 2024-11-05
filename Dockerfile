FROM openjdk:18
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} Tingeso-0.0.1-SNAPSHOT.jar
EXPOSE 8090
ENTRYPOINT ["java","-jar","/Tingeso-0.0.1-SNAPSHOT.jar"]