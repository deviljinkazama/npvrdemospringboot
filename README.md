# npvrdemospringboot
npvr demo of springboot


>java -jar target\npvrdemospringboot-1.0.jar

Request : http://localhost:8080/npvr/record

Content-Type = application/json

POST request

request body :
{
    "channelId" : "8",
    "programId" :"7",
    "startTime" :"02/04/2014 23:37:50"
}


Response Like :

{
  "record": {
    "channelId": "8",
    "programId": "7",
    "startTime": "02/04/2014 23:37:50",
    "endTime": "03/04/2014 00:07:50"
  },
  "channel": {
    "channelId": "8",
    "callSign": "CHAN8",
    "fallBack": false
  },
  "programme": {
    "progId": "7",
    "progName": "PROGRAMME7",
    "progDuration": 30
  },
  "status": "Success"
}



GET request
http://localhost:8080/npvr/hi



