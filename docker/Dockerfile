FROM ubuntu:16.04

MAINTAINER Jake Lee <bwv1050@gmail.com>

COPY indiepost.conf /etc/nginx/conf.d/
VOLUME ["/etc/nginx"]

RUN apt-get update && apt-get install -y software-properties-common python-software-properties
RUN \
  echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | debconf-set-selections && \
  add-apt-repository -y ppa:webupd8team/java && \
  apt-get update && \
  apt-get install -y oracle-java8-installer && \
  rm -rf /var/lib/apt/lists/* && \
  rm -rf /var/cache/oracle-jdk8-installer

ENV JAVA_HOME /usr/lib/jvm/java-8-oracle
ADD new-indiepost.jar /webapps/indiepost.jar
WORKDIR /webapps

ENTRYPOINT ["java", "-jar", "indiepost.jar"]

EXPOSE 80
EXPOSE 443
