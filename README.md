# SpringBlogDemo

A simple simple Spring Boot app that I created for parcticing purposes. 
The app represents a simple blog wich realizes CRUD functionalities for Comments, Posts and Users.
Also suports CSV and XML export/import adn implements basic authentication.
Full funtcional can be viewed on http://localhost:8080/swagger-ui/index.html# using swagger-ui.

## Technologies:
- Java
- Spring Boot
- PostgreSQL

### Dependencies
- Liquibase
- Swagger
- opencsv
- ItextPDF
- Minio

## How to run

### Run using docker
```
docker-compose up -p
```

### Run using maven
```
mvn spring-boot:run
```
