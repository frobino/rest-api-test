# JAX-RS test app (using Jersey and maven)

A simple REST server.

## How to create an initial web app skeleton using mvn

```
mvn archetype:generate -DgroupId=io.github.frobino.jaxrs-test-app -DartifactId=jaxrs-test-app -DarchetypeArtifactId=maven-archetype-webapp -DinteractiveMode=false
```

## How to build and run the server

Create war file under target/ folder:
```
mvn clean
mvn package
```

Copy the generated war file in the tomcat web server:
```
cp target/jaxrs-test-app.war ~/Tools/apache-tomcat-9.0.41/webapps/
```

Start/stop the tomcat server:
```
cd ~/Tools/apache-tomcat-9.0.41/bin/
export CATALINA_HOME=/home/<user>/Tools/apache-tomcat-9.0.41
export JAVA_HOME=/usr/lib/jvm/java-1.11.0-openjdk-amd64
./startup.sh
./shutdown.sh
```

Go to ocalhost:8080/jaxrs-test-app/

```
http://localhost:8080/jaxrs-test-app/crunchify/ctofservice/
Expected 98.24

http://localhost:8080/jaxrs-test-app/crunchify/ctofservice/35
Expected 95.0

http://localhost:8080/jaxrs-test-app/crunchify/ftocservice/
Expected 36.8

http://localhost:8080/jaxrs-test-app/crunchify/ftocservice/98
Expected 36.666
```

## References

[maven intro](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html)

[not up-to-date but concise example of jaxrs (jersey 1.0) + maven](https://crunchify.com/how-to-build-restful-service-with-java-using-jax-rs-and-jersey/)

[up-to-date example of jaxrs (jersey 2.7) + maven](https://github.com/naveenvemulapalli/test-jersey-rest-maven-tomcat)

[add CORS to jaxrs (jersey) server](https://crunchify.com/what-is-cross-origin-resource-sharing-cors-how-to-add-it-to-your-java-jersey-web-server/)

[implement CRUD using jaxb](https://www.vogella.com/tutorials/REST/article.html)