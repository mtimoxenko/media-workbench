# Media-Workbench

## Overview
Media-Workbench is a comprehensive task management system designed for Network Operations Centers (NOCs), focusing on multimedia workflows and efficient operations management. This platform provides a robust solution for handling diverse tasks, monitoring operations, and managing multimedia content seamlessly.

## Key Features
- **Advanced Task Management**: Organize, track, and update tasks with ease.
- **Backend Efficiency**: Robust Java backend utilizing Spring Boot for optimal performance.
- **DevOps Integration**: Streamlined deployment with Docker, Terraform, and Ansible.
- **Real-time Operations**: Facilitate real-time tracking and updates of tasks.
- **Scalable Architecture**: Designed to scale with your team and project needs.


## Getting Started
Begin by cloning the repository and setting up the development environment:


## Installation
```bash
git clone https://github.com/your-repository/Media-Workbench.git
cd Media-Workbench
# Follow specific setup instructions
```

## Running the Application with Docker Compose

To start the application using Docker Compose, follow these steps:

1. Ensure Docker and Docker Compose are installed on your system.
2. Open a terminal and navigate to the root directory of the project where the `docker-compose.yml` file is located.
3. Run the following command to build and start the containers in the background:

```bash
docker-compose up -d
```

To view the logs of the running services, use:
```bash
docker-compose logs -f
```

When you are done, you can stop and remove the containers with the command:
```bash
docker-compose down
```

Visit http://localhost:3000 to access the frontend and http://localhost:8080 for the backend services.

## Additional Features

### H2 Console for Testing
For development and testing purposes, the H2 console can be accessed at:
http://localhost:8080/h2-console/

This provides a convenient web interface for interacting with the H2 database used by the backend.

### OpenAPI Definitions
Explore the backend's API with the OpenAPI definitions available at:
http://localhost:8080/swagger-ui/index.html

This interface facilitates easy testing and interaction with the API endpoints.


## Testing Credentials

To log in to the application during development or testing, you can use the following credentials:

| Email                     | Password          | Role          | Shift     |
|---------------------------|-------------------|---------------|-----------|
| deckard@bladerunner.com   | voightkampff      | BLADE_RUNNER  | NIGHT     |
| k@bladerunner2049.com     | baseline          | BLADE_RUNNER  | NIGHT     |
| batty@nexus6.com          | morelife          | REPLICANT     | DAY       |
| pris@nexus6.com           | basicpleasure     | REPLICANT     | DAY       |
| sebastian@tyrell.com      | chessmaster       | ENGINEER      | EVENING   |
| tyrell@tyrell.com         | replicantcreator  | ENGINEER      | EVENING   |



## Contributing
Contributions to Media-Workbench are welcome.

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE.md) file for details.

## Contact
For any queries or suggestions, please reach out to project maintainers at mtimochenko@tutanota.com.


## Entity-Relationship (ER) Model
Below is the ER model of the Media-Workbench project:
![ER Model](_docs/er_diagram.png)