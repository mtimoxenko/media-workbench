# Backend Service for Media-Workbench

## Overview
The backend service for Media-Workbench is the core engine driving the platform's functionality. It handles API requests, manages the database, and executes business logic. Developed with a focus on performance and scalability, the backend service uses Spring Boot and is designed to work seamlessly with the frontend component and CI/CD workflows.

## Key Features
- **Spring Boot Framework**: Leverages Spring Boot for rapid development and high performance.
- **RESTful API**: Provides a RESTful API for frontend-backend communication.
- **H2 Database Integration**: Includes an in-memory H2 database for testing and development.
- **Swagger/OpenAPI**: Utilizes Swagger for API documentation and interactive exploration.

## Getting Started
These instructions will get you a copy of the backend service up and running on your local machine for development and testing purposes.

### Prerequisites
- Java 20
- Maven (for dependency management and running the project)
- Docker (optional, for containerization)

### Installation
Clone the repository and navigate to the backend service directory:
```bash
git clone https://gitlab.com/mtimoxenko/media-workbench
cd media-workbench/back
```


### Running the Service with Docker
A `Dockerfile` is provided in the `back` directory. To build the Docker image and run the service in a Docker container, execute the following commands:
```bash
docker build -t media-workbench-back .
docker run -d -p 8080:8080 media-workbench-back
```
This will start the backend service inside a Docker container and expose it on `http://localhost:8080`.

## Stopping the Service
To stop the running Docker container for the backend service, you can use the following command:

```bash
docker stop media-workbench-back
```

### H2 Console for Database Testing
The H2 database console is configured for testing purposes and can be accessed at:
http://localhost:8080/h2-console/

### Swagger API Documentation
Access the Swagger UI to interact with the API at:
http://localhost:8080/swagger-ui/index.html

## Entity-Relationship (ER) Model
To understand the data model behind the Media-Workbench backend service, refer to the ER diagram below:
![ER Model](../_docs/er_diagram.png)

## Built With
- [Spring Boot](https://spring.io/projects/spring-boot) - The framework used
- [Maven](https://maven.apache.org/) - Dependency management
- [H2 Database](https://www.h2database.com/) - Embedded in-memory database
- [SpringDoc OpenAPI](https://springdoc.org/) - API documentation
- [Lombok](https://projectlombok.org/) - Java library for reducing boilerplate code


## License
This project is licensed under the MIT License - see the [LICENSE.md](../LICENSE.md) file for details.

## Contact
For any queries or suggestions related to the backend service, please reach out to the backend team at [mtimochenko@tutanota.com].