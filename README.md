# SpringBlogDemo

A simple Spring Boot app that I created for practicing purposes. 
The app represents a simple blog that realizes CRUD functionalities for Comments, Posts and Users.
Also supports CSV and XML export/import and implements basic authentication.
Full functional can be viewed on http://localhost:8080/swagger-ui/index.html# using swagger-ui.

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
