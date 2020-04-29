# General-purpose image with SBT
#
# For more inspiration, check out: https://github.com/hseeberger/scala-sbt/blob/master/oracle/Dockerfile

FROM openjdk:8u212-jdk-alpine

ARG SCALA_VERSION
ENV SCALA_VERSION ${SCALA_VERSION:-2.11.8}
ARG SBT_VERSION
ENV SBT_VERSION ${SBT_VERSION:-0.13.18}

RUN apk add --no-cache bash

# Install SBT

WORKDIR /opt

RUN wget -O - "https://github.com/sbt/sbt/releases/download/v$SBT_VERSION/sbt-${SBT_VERSION}.tgz" | \
    tar xzf -

ENV PATH=$PATH:/opt/sbt/bin

# Download sbt and Scala versions

WORKDIR /root

RUN /opt/sbt/bin/sbt sbtVersion && \
    /opt/sbt/bin/sbt "set scalaVersion := \"$SCALA_VERSION\"" console && \
    rm -rf project target

# Mount source code to /app!

WORKDIR /app
CMD ["/opt/sbt/bin/sbt"]
