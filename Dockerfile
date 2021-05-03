FROM openjdk:15.0.2
LABEL maintainer="alexander.dobrynin@th-koeln.de"

COPY target/universal/schedule-1.0 /schedule
CMD /schedule/bin/schedule