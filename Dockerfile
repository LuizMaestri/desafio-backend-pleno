FROM openjdk:11.0.3-jre-slim
MAINTAINER Luiz Maestri <luizricardomaestri@gmail.com>

ENTRYPOINT exec java $JAVA_OPTS -jar /usr/share/habitans/habitants.jar -Djava.net.preferIPv4Stack=true

ADD target/habitants-*.jar /usr/share/habitans/habitants.jar

EXPOSE 8080