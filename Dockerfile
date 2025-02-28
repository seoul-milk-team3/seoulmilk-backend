FROM amazoncorretto:17.0.7-alpine
COPY build/libs/be-0.0.1-SNAPSHOT.jar seoulmilk.jar

ENV TZ Asia/Seoul
ARG ENV

ENTRYPOINT ["java", "-jar","-Dspring.profiles.active=prod", "-Dserver.env=${ENV}", "seoulmilk.jar"]