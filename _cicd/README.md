# CI/CD Pipelines for Media-Workbench

## Overview
This document outlines the Continuous Integration and Continuous Deployment (CI/CD) pipelines for the Media-Workbench project. The CI/CD process ensures that new code changes are automatically built, tested, and deployed, enabling a streamlined workflow from development to production.

## Pipelines Overview
The project is configured with the following CI/CD pipelines:

1. **Build Infrastructure**: Automates the provisioning of cloud resources using Terraform.
2. **Configure Infrastructure**: Utilizes Ansible to configure the provisioned infrastructure.
3. **Build & Deploy Frontend**: Handles the building of the frontend Docker image and its deployment.
4. **Build & Deploy Backend**: Manages the building of the backend Docker image and its deployment.

Each pipeline is triggered by specific GitLab CI/CD rules, ensuring that only relevant changes initiate the CI/CD process.

## Stages and Jobs

### Terraform Pipelines
- **prepare_tf**: Prepares the Terraform configuration.
- **validate_tf**: Validates the Terraform files.
- **plan_tf**: Executes a Terraform plan to show potential changes.
- **deploy_tf**: Applies the Terraform configuration to provision resources.
- **destroy_tf**: (Manual) Destroys the provisioned infrastructure when necessary.

### Ansible Configuration
- **configure_infra**: Runs Ansible playbooks to configure servers and services.

### Docker Image Building and Deployment
- **build_frontend/backend**: Builds Docker images for the frontend/backend and pushes them to a Docker registry.
- **deploy_frontend/backend**: Deploys the Docker images to the server and starts the containers.

## Trigger Rules
Each pipeline has specific rules for when it is triggered:

- Build infrastructure is initiated on changes to Terraform files in the main branch.
- Configure infrastructure runs on changes to Ansible configuration files in the main branch.
- Frontend and backend pipelines are triggered by tags matching `/frontend/` and `/backend/`, respectively.

## Terraform State Management
Terraform states are stored in GitLab to ensure consistency and versioning of the infrastructure as code.

## Manual Actions
The destruction of infrastructure via Terraform is a manual action to prevent accidental deletions.

## Ansible Playbooks
Ansible is used to configure the server with necessary packages, Docker, and Docker Compose, ensuring that the application is deployed in a consistent environment.

## Docker in Docker (DinD)
Docker images are built using the DinD service, which allows Docker to run inside a Docker container.

## Artifacts and Dependencies
Artifacts from each job are passed along to subsequent jobs when necessary, ensuring that, for example, the IP address of a newly provisioned server is available to the Ansible configuration job.

## Contributing to CI/CD
Contributions to the CI/CD process are welcome. Please adhere to the project's guidelines for submitting changes to the pipeline configurations.

## Contact
For any queries or suggestions related to the CI/CD processes, please reach out to the CI/CD team at [mtimochenko@tutanota.com].

## License
This project is licensed under the MIT License - see the [LICENSE.md](../LICENSE.md) file for details.
