FROM amazonlinux:latest

MAINTAINER Jake Lee <bwv1050@gmail.com>

RUN yum install -y java-1.8.0
RUN yum remove -y java-1.7.0-openjdk

COPY new-indiepost.jar /webapps/indiepost.jar
WORKDIR /webapps

CMD ["java", "-jar", "indiepost.jar"]

EXPOSE 8080
