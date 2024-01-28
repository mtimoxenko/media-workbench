# Frontend Service for Media-Workbench

## Overview
The frontend service for Media-Workbench provides the user interface for the platform, enabling users to interact with the task management system efficiently. It is built with HTML, CSS, and JavaScript, ensuring compatibility across all modern web browsers without the need for additional frameworks or build tools.

## Key Features
- **User-Friendly Interface**: A clean and intuitive interface that simplifies task management.
- **Interactive Task Cards**: Users can create, assign, and track tasks in real-time.
- **Shift-Based Task Viewing**: Allows users to view tasks segmented by day, night, or evening shifts.
- **Role-Based Access Control**: Users can see and do only what their role permits within the system.

## Getting Started
These instructions will get you a copy of the frontend service up and running on your local machine for development and testing purposes.

### Prerequisites
- A modern web browser

### Installation
Clone the repository and navigate to the frontend service directory:
```bash
git clone https://gitlab.com/frem3n/media-workbench
cd media-workbench/front
```

### Running the Service with Docker

A `Dockerfile` is provided in the `front` directory, along with an Nginx configuration file that sets up a reverse proxy to the backend service. To build the Docker image and run the service in a Docker container with the reverse proxy, execute the following commands:

```bash
docker build -t media-workbench-front .
docker run -d -p 3000:80 media-workbench-front
```

This will start the frontend service inside a Docker container, serving static files with Nginx and proxying API requests to the backend service. The service will be exposed on `http://localhost:3000`.

#### Nginx Reverse Proxy

The Nginx server within the Docker container is configured to route API requests to the backend. Any request made to `/api/` will be proxied to the backend service, allowing the frontend to communicate with the backend seamlessly as if it were a single service.

For example, when the frontend performs a login request to `/api/users/login`, Nginx will forward this request to the backend's `/users/login` endpoint. This setup abstracts the backend's URL structure and provides a cleaner separation between the frontend and backend services.

Be sure to replace the example URLs with the actual URL of your backend service when deploying in a production environment.

## Stopping the Service
To stop the running Docker container for the frontend service, you can use the following command:

```bash
docker stop media-workbench-front
```

## Built With
- HTML, CSS, and JavaScript - Core frontend technologies
- [Nginx](https://nginx.org/) - High-performance web server used to serve the static files


## License
This project is licensed under the MIT License - see the [LICENSE.md](../LICENSE.md) file for details.

## Contact
For any queries or suggestions related to the frontend service, please reach out to the frontend team at [mtimochenko@tutanota.com](mailto:mtimochenko@tutanota.com).
