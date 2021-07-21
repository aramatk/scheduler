FROM gradle:7.1.1-jdk11 as builder
USER root
WORKDIR /builder
ADD . /builder
RUN gradle bootJar

FROM adoptopenjdk:11-jre-hotspot
WORKDIR /app
EXPOSE 5009
COPY --from=builder /builder/build/libs/scheduler-0.0.1-SNAPSHOT.jar .
ENTRYPOINT [ "java", "-jar", "scheduler-0.0.1-SNAPSHOT.jar" ]
