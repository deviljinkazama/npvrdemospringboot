FROM openjdk:8
EXPOSE 8080 8888
RUN pwd
RUN mkdir shardool
RUN ls
RUN echo
RUN echo "Change @11:32AM 29-5-2017"
RUN curl http://172.17.0.11/npvr.jar -o /shardool/npvr.jar
CMD java -jar /shardool/npvr.jar
