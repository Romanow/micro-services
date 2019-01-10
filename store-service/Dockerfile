FROM java:8

VOLUME /tmp

ADD build/libs/store-service.jar store-service.jar

RUN bash -c 'touch /store-service.jar'

EXPOSE 8090

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/store-service.jar"]
