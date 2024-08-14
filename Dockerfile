FROM sbtscala/scala-sbt:eclipse-temurin-jammy-21.0.2_13_1.10.1_3.3.3 as sbt
ARG GITHUB_TOKEN
ENV GITHUB_TOKEN=$GITHUB_TOKEN
WORKDIR /schedule
COPY . .
RUN sbt clean stage

FROM eclipse-temurin:21.0.2_13-jre-jammy
WORKDIR /schedule
RUN mkdir bootstrap
COPY --from=sbt /schedule/target/universal/stage .
COPY --from=sbt /schedule/bootstrap/Mapping.csv bootstrap/Mapping.csv
CMD bin/schedule -Dconfig.file=conf/application-prod.conf