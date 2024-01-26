# Media-Workbench

## Overview
Media-Workbench is an intuitive and powerful task management system built to centralize and manage tasks across teams, particularly suited for Network Operations Centers (NOCs). It streamlines the assignment and tracking of tasks, empowering teams to create, assign, and manage workloads efficiently. With Media-Workbench, you can create tasks from scratch or leverage pre-designed templates, assign tasks to teams segmented by shifts—day, night, or evening—or directly to team members, including self-assignment for personal tracking. Each task's progress is transparently tracked, with the ability to add comments and view the latest updates directly on the task card. Completed tasks shift into a log where comments and histories are preserved, ensuring a comprehensive view of past activities. The application also designates roles to users, with each role outlining key responsibilities and capabilities within the system, ensuring clear delineations of duties and streamlined workflow management.

## Key Features
- **Task Management**: Centralize and manage tasks across different teams and shifts with a user-friendly interface.
- **Template and Custom Tasks**: Start new tasks using predefined templates or create custom tasks to meet specific needs.
- **Dynamic Task Assignment**: Assign tasks to teams based on shift timings, to individual team members, or to yourself.
- **Real-time Tracking and Updates**: Progress on tasks is updated in real-time with the ability for team members to add comments and view recent activities directly on task cards.
- **Comment History**: Maintain a log of the latest comments for ongoing tasks and a comprehensive history for completed tasks.
- **Role-Based Access**: Define user roles with specific responsibilities, enhancing structure and clarity within your team's operations.
- **Shift Segmentation**: Organize workload and task visibility based on day, night, and evening shifts, optimizing 24/7 operations.
- **Backend Efficiency**: A robust Java backend powered by Spring Boot ensures optimal performance and reliability.
- **DevOps Integration**: Seamless deployment process with Docker, Terraform, and Ansible for streamlined operations.
- **Scalable Architecture**: Built to grow with your team and project needs, ensuring that your management system adapts to your evolving requirements.



## Getting Started
Begin by cloning the repository and setting up the development environment:


## Installation
```bash
git clone https://gitlab.com/frem3n/media-workbench
cd media-workbench
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

Visit http://localhost:3000 to access the frontend

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


## Entity-Relationship (ER) Model
Below is the ER model of the Media-Workbench project:
![ER Model](_docs/er_diagram.png)


## Contributing
Contributions to Media-Workbench are welcome.

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE.md) file for details.

## Contact
For any queries or suggestions, please reach out to project maintainers at mtimochenko@tutanota.com.