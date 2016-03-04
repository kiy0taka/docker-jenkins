FROM jenkins
MAINTAINER Kiyotaka Oku

USER root
RUN apt-get update && apt-get install -y fonts-ipafont-gothic

USER jenkins
COPY src/script /usr/share/jenkins/ref/init.groovy.d
