FROM openjdk:11-jdk

VOLUME /tmp

ADD build/libs/warehouse-service.jar warehouse-service.jar

RUN bash -c 'touch /warehouse-service.jar'

EXPOSE 8060

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/warehouse-service.jar"]
