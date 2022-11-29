
###Builder Image###
FROM maven:3.6.3-jdk-11-alpine-1 as builder

COPY . /app/
WORKDIR /app/

# Build maven application
RUN mvn clean package

RUN mv target/*.jar app.jar

###Executable Image###
FROM openjdk:11-alpine
COPY --from=builder /app/app.jar /app/app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","app.jar"]
