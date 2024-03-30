FROM openjdk:11

ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /ect/localtime && echo $TZ > /etc/timezone

COPY *.jar /app.jar

ENTRYPOINT ["java","-jar","/app.jar"]