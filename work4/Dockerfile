FROM openjdk:11

ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /ect/localtime && echo $TZ > /etc/timezone

COPY *.jar /app.jar

CMD ["--server.port=8080"]

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app.jar"]