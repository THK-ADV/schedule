FROM sbtscala/scala-sbt:eclipse-temurin-jammy-17.0.5_8_1.8.3_2.13.10 as sbt
ARG GITHUB_TOKEN
ENV GITHUB_TOKEN=$GITHUB_TOKEN
WORKDIR /schedule
COPY . .
RUN sbt clean stage

FROM openjdk:17.0.2-slim
WORKDIR /schedule
RUN mkdir bootstrap
COPY --from=sbt /schedule/target/universal/stage .
COPY --from=sbt /schedule/bootstrap/Mapping.csv bootstrap/Mapping.csv
CMD bin/schedule -Dconfig.file=conf/application-prod.conf