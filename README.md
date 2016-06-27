## Microservices with Spring Cloud ##

Code samples as of the [talk held at 22nd of June at developer week 2016 in Nuremberg](http://www.slideshare.net/DanielEichten/microservices-with-spring-cloud-63291299) by Paul Vassu and Daniel Eichten.

All samples should build if you have Java 8 locally installed and just type `./mvnw clean spring-boot:run` (Linux, Mac) or
`mvnw.cmd clean spring-boot:run` (Windows). The samples are structured after the steps demoed. Be aware that for the later 
samples you have to have a MySQL Database running locally as done in the demo using a docker container, as well as you might 
want to use a forked and own version of the config repo providing your local settings, e.g. your API key for OpenWeatherMap.

**General Note** The used API key for OpenWeatherMap is invalid. Please go to [there](http://openweathermap.org), register yourself and request a key. Basic usage is [free](http://openweathermap.org/price).

### 1 Monolith ###
The is the start of the journey. Everything is contained in one single app and serving statics as well as services using an 
in-mem DB and the connection to OpenWeatherMap.

This example will use an in-mem h2 database and therefore no local DB. But be aware that data will be lost after restart of the app.

### 2 Simple Split ###
As a first refactoring step the service offering real services as well as the server serving static files are being split-up 
and exposed though a single reverse proxy.

Persistance will only be an in-mem h2 database.

### 3 Introducing Registry ###
To eliminate the need to configure everything statically we do introduce a registry server that helps the various other services 
to register and discover each other. This example also will make a use of an non-in-mem DB but will allow to run multiple instaces 
of each service behind Zuul by telling the services to just use a random port that is free.

**Attention** In order to run this as well as the subsequent examples you'll have to have a MySQL database running. For demo purposes we used the official docker image of MySQL 5.7. If you want to switch to a different DB you may also do this by exchanging the SQL driver dependency in the `pom.xml` of the services part – all the examples are using Spring Data JPA (with Hibernate as provider) and should therefore run with any DB supported. 

Also be aware that you should start the services in a specific order. To mitigate any potential issues you should start the registry always first. Also note that you may run multiple instances of the services part cause it is configured to pick a random port.

### 4 Further Split up ###
To further split up the services by purpose we split the single service app of step 3 into one service offering member CRUD ops as 
well as one service talking to OpenWeatherMap.

**Attention** As in step 3 you should run the registry first. 

### 5 Production Ready Setup ###
In the final step the same setup as in 4 is used but everything supported by distributed tracing using Zipkin as well as Turbine 
Collector getting all hystrix streams of all components and reporting accordingly.

When opening the turbine collector be sure to add the default cluster identifier to the turbine stream. Otherwise only parts of your cluster will show up (usually the service named first – so gateway in this case). [link to open](http://localhost:5001/turbine.stream?cluster=default)

**Attention** In order to run this example you will have to start the config server **always** first and should start the registry as a second service when the config server successfully started and is showing as `UP` on [http://localhost:8888/health](http://localhost:8888/health).
