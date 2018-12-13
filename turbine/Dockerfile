FROM java:8

VOLUME /tmp
VOLUME /var/log/turbine

ADD build/libs/turbine.jar turbine.jar

RUN bash -c 'touch /turbine.jar'
RUN bash -c 'mkdir -p /var/log/turbine'

EXPOSE 8880

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/turbine.jar"]
