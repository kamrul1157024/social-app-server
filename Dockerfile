FROM ubuntu:20.04

RUN apt-get update && \
    apt-get install -y openjdk-11-jdk && \
    apt-get install -y maven && \
    apt-get clean;

RUN apt-get update && \
    apt-get install ca-certificates-java && \
    apt-get clean && \
    update-ca-certificates -f;

RUN apt-get install -y curl

WORkDIR /app/server
COPY . /app/server

RUN mvn clean install -DskipTests
RUN mvn compile
