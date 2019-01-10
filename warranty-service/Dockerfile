FROM java:8

VOLUME /tmp

ADD build/libs/warranty-service.jar warranty-service.jar

RUN bash -c 'touch /warranty-service.jar'

EXPOSE 8050

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/warranty-service.jar"]
