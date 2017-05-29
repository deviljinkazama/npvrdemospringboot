FROM openjdk:8
EXPOSE 8080 8888
RUN pwd
RUN mkdir shardool
RUN ls
Run echo 
RUN curl http://172.17.0.11/npvr.jar -o /shardool/npvr.jar
CMD java -jar /shardool/npvr.jar
