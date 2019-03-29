## Info

I decided to use Spring Boot and H2 as it is a perfect combo to deliver ready-to-run applications that don't require any configuration in your side, just run it and it will work. Spring Boot uses an embedded tomcat 8 instance and H2 (In-Memory database) creates a database stored in-memory that is automatically configured on startup based on the @Entity classes inside the project. Given its nature, database structure and data are lost when the server is stopped.

## Note

All informations are saved in the database ONLY if they represent a valid instance of the problem (correct room size, starting position, patches position and instructions).
If the input is wrong a 400 HTTP status is returned with a similar body depending on what went wrong:

```javascript
{
  "error_codename" : "INVALID_INSTRUCTION",
  "error_message" : "The concatenated instruction string can only contain the following characters: N,S,W,E."
}
```

## Requirements

Only Java8 is required to run the server. The database is created automatically in-memory.

## Startup

cd ~/git/yoti-sdk-backend-test/
java -jar yoti-sdk-backend-test.jar

## Endpoints:

Main endpoint is http://localhost:8080/roomba and accepts POST requests.

A GUI with all the database informations can be reached at http://localhost:8080/h2 
Use the following credentials when prompted:

Driver class: org.h2.Driver
JDBC url: jdbc:h2:file:~/test_db
User name: sa
Password:
