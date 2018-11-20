FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG DEPENDENCY=target/configuration
COPY ${DEPENDENCY} /app
ENTRYPOINT ["java","-cp","/app/WEB-INF/lib/*:/app/WEB-INF/classes/.","com.bst.configuration.application.Application"]