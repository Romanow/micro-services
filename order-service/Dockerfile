FROM openjdk:11-jdk

VOLUME /tmp

ADD build/libs/order-service.jar order-service.jar

RUN bash -c 'touch /order-service.jar'

EXPOSE 8070

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/order-service.jar"]
