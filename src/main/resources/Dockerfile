FROM centos:7
MAINTAINER wzg
ADD jdk-18_linux-aarch64_bin.tar.gz /usr/docker/java
ENV JAVA_HOME /usr/docker/java/jdk-18
ENV JRE_HOME $JAVA_HOME/jre
ENV CLASSPATH=:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar:$JRE_HOME/lib
ENV PATH=$PATH:$JAVA_HOME/bin:$JRE_HOME/bin
COPY *.jar /app.jar
EXPOSE 1024
ENTRYPOINT ["java", "-jar", "./app.jar", ">", "./log/app.log", "--spring.config.additional-location=/application-dev.yml"]
