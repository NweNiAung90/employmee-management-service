FROM openjdk:12-alpine
WORKDIR /opt/app/
COPY target /opt/app/

EXPOSE 8077
CMD ["java", "-jar", "employee-management-service-1.0.0.jar"]