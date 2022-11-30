
###Builder Image###
FROM maven:3-openjdk-8 as builder

COPY . /app/
WORKDIR /app/

# Build maven application
RUN mvn clean package

RUN mv target/*.jar app.jar

###Executable Image###
WORKDIR /app
FROM openjdk:8-jdk-alpine
COPY --from=builder /app/app.jar .
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","app.jar"]
