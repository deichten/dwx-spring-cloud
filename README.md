## Microservices with Spring Cloud ##

Code samples as of the talk held at 22nd of June at developer week 2016 in Nuremberg by Paul Vassu and Daniel Eichten.

All samples should build if you have Java 8 locally installed and just type `./mvnw clean spring-boot:run` (Linux, Mac) or
`mvnw.cmd clean spring-boot:run` (Windows). The samples are structured after the steps demoed. Be aware that for the later 
samples you have to have a MySQL Database running locally as done in the demo using a docker container, as well as you might 
want to use a forked and own version of the config repo providing your local settings, e.g. your API key for OpenWeatherMap.

Also be aware that you might need to run the services in a specific order.

### 1 Monolith ###
The is the start of the journey. Everything is contained in one single app and serving statics as well as services using an 
in-mem DB and the connection to OpenWeatherMap.

### 2 Simple Split ###
As a first refactoring step the service offering real services as well as the server serving static files are being split-up 
and exposed though a single reverse proxy.

### 3 Introducing Registry ###
To eliminate the need to configure everything statically we do introduce a registry server that helps the various other services 
to register and discover each other. This example also will make a use of an non-in-mem DB but will allow to run multiple instaces 
of each service behind Zuul by telling the services to just use a random port that is free.

### 4 Further Split up ###
To further split up the services by purpose we split the single service app of step 3 into one service offering member CRUD ops as 
well as one service talking to OpenWeatherMap.

### 5 Production Ready Setup ###
In the final step the same setup as in 4 is used but everything supported by distributed tracing using Zipkin as well as Turbine 
Collector getting all hystrix streams of all components and reporting accordingly.
