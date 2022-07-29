FROM hseeberger/scala-sbt:11.0.14.1_1.6.2_2.13.8
LABEL maintainer="alexander.dobrynin@th-koeln.de"

WORKDIR /schedule
COPY . .
RUN sbt clean stage
CMD target/universal/stage/bin/schedule -Dconfig.file=conf/application-prod.conf