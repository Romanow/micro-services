FROM java:8

VOLUME /tmp
VOLUME /var/log/eureka

ADD build/libs/eureka.jar eureka.jar

RUN bash -c 'touch /eureka.jar'
RUN bash -c 'mkdir -p /var/log/eureka'

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/eureka.jar"]
