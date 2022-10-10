<h1 align="center">
  Parking Control
</h1>

<h3 align="center">
  Parking management system application
</h3>

<p align="center">
  <a href="https://github.com/alexbraga/parking-control/commits/master"><img alt="GitHub last commit" src="https://img.shields.io/github/last-commit/alexbraga/parking-control"></a>
  <!-- <a href="https://heroku.com"><img alt="Heroku build status" src="https://img.shields.io/github/deployments/alexbraga/parking-control/parking-control-server?label=heroku&logo=heroku"></a> -->
  <!-- <a href="https://app.netlify.com"><img alt="Netlify build status" src="https://img.shields.io/netlify/d51f97e2-2c56-44a0-9b2d-bb4449b84098?logo=netlify"></a> -->
  <!-- <a href="https://github.com/alexbraga/parking-control/blob/master/LICENSE"><img alt="GitHub license" src="https://img.shields.io/github/license/alexbraga/parking-control?label=license"></a> -->
</p>

<h4 align="center">
	 Status: In Development
</h4>

<p align="center">
 <a href="#about">About</a> •
 <a href="#features">Features</a> •
 <a href="#todo">Todo</a> •
 <a href="#how-it-works">How it works</a> •
 <a href="#tech-stack">Tech Stack</a> •
 <a href="#how-to-contribute">How to contribute</a> •
 <a href="#author">Author</a> <!--•
 <a href="#license">License</a> -->
</p>

## About

<p align="justify">Parking management Spring Boot REST API with PostgreSQL database and Object-Relational Mapping made with Spring Data JPA (Hibernate). The application features unit tests for dtos, services and controllers, following TDD practices.</p>

---

## Features

- REST API
- Relational database (PostgreSQL)
- Object-Relational Mapping
- JPA with Hibernate
- CORS
- Layered structure divided into Entities, Repositories, Services and Controllers
- Unit Tests with JUnit and Mockito

---

## Todo

- [x] Fix update method for ParkingSpotController
- [ ] Fix registration date format for parking spot
- [ ] Complete endpoints section on README
- [ ] Add integration tests

---

## How it works

1. <a href="#clone-this-repository">Clone this repository</a>
2. <a href="#set-the-environment-variables">Set the environment variables</a>
3. <a href="#running-the-application">Run the application</a>

#### Pre-requisites

Before getting started, you will need to have the following tools installed on your machine:

- [Git](https://git-scm.com)
- [Java OpenJDK](https://www.oracle.com/java/technologies/downloads/)
- [Maven](https://maven.apache.org/)

In addition, you might also want an IDE to work with the code, like
[IntelliJ IDEA](https://www.jetbrains.com/idea/).

#### Clone this repository

```
git clone https://github.com/alexbraga/parking-control.git
```

#### Set the environment variables
- Create `parking-control/server/src/main/resources/application.properties` and set the environment variables:

```
spring.datasource.url= jdbc:postgresql://localhost:5432/parking-control-db
spring.datasource.username=postgres
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update

spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=true
```

#### Running the application

```bash
# Go to the application root folder
$ cd parking-control/

# Install the dependencies
$ mvn dependency:resolve

# Compile and run the application
$ mvn spring-boot:run
```

- Alternatively, open `parking-control/` with your preferred IDE and run `ParkingControlApplication.java`

- The server will start at `localhost:8080`

#### API Endpoints

Soon

---

## Tech Stack

The following tools were used in the construction of the project:

#### **Language**

- **[Java OpenJDK 11](https://www.oracle.com/java/technologies/downloads/)**

#### **Framework**

- **[Spring](https://spring.io/)**

#### **Dependencies**

- Spring Web
- Spring Data JPA
- SpringBoot Validation
- PostgreSQL
- Hamcrest

> See the file
> [pom.xml](https://github.com/alexbraga/parking-control/blob/master/server/pom.xml)

#### **Utilities**

- Dependency Manager: **[Maven](https://maven.apache.org/)**
- IDE: **[IntelliJ IDEA](https://www.jetbrains.com/idea/)**
- API Testing: **[Postman](https://postman.com)**

---

## How to contribute

1. Fork the project
2. Create a new branch with your changes:
```
git checkout -b my-amazing-feature
```
3. Save your changes and create a commit message (in present tense) telling what you did:
```
git commit -m "Add my amazing feature"
```
4. Submit your changes:
```
git push origin my-amazing-feature
```
5. Create a pull request

---

## Author

<p>Alexandre Braga</p>

[![Twitter Badge](https://img.shields.io/badge/-@_alex_braga-1ca0f1?style=flat-square&labelColor=1ca0f1&logo=twitter&logoColor=white)](https://twitter.com/_alex_braga)
[![Linkedin Badge](https://img.shields.io/badge/-Alexandre%20Braga-blue?style=flat-square&logo=Linkedin&logoColor=white)](https://www.linkedin.com/in/alexgbraga/)
[![Gmail Badge](https://img.shields.io/badge/-contato@alexbraga.com.br-c14438?style=flat-square&logo=Gmail&logoColor=white)](mailto:contato@alexbraga.com.br)

---

<!-- ## License

This project is under the [MIT License](./LICENSE). -->

