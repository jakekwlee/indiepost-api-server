FROM fabric8/java-jboss-openjdk8-jdk:latest
MAINTAINER Jake Lee <bwv1050@gmail.com>

ENV AB_OFF true
ENV TZ Asia/Seoul
COPY indiepost-backend.jar /webapps/indiepost.jar
WORKDIR /webapps

CMD ["java", "-jar", "indiepost.jar"]

EXPOSE 8080
