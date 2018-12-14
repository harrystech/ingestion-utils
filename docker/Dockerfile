#
# Provides the base image for Hyppo build containers, and run-time containers for local testing.
#
FROM openjdk:8-jdk-alpine

ARG SBT_VERSION=0.13.18
ARG SCALA_VERSION=2.11.8

WORKDIR /root
RUN echo "http://dl-cdn.alpinelinux.org/alpine/edge/community" >> /etc/apk/repositories \
    && apk update \
    && apk add --no-cache ca-certificates bash wget \
    && update-ca-certificates
RUN wget --quiet --output-document sbt.tgz https://github.com/sbt/sbt/releases/download/v${SBT_VERSION}/sbt-${SBT_VERSION}.tgz
RUN tar xzf sbt.tgz && \
    rm sbt.tgz && \
    mv sbt* /usr/local/sbt
# Forces SBT to download so it gets cached in this layer, speeding up builds
RUN /usr/local/sbt/bin/sbt "set scalaVersion := \"$SCALA_VERSION\"" console && \
    rm -rf project target

WORKDIR /app

ENTRYPOINT ["bash"]
