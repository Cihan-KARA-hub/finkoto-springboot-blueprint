Charging Station Simulation Platform
This project provides a simulated environment for managing and testing charging stations using the OCPP (Open Charge Point Protocol). Testing a real-world OCPP-compliant system is often costly and time-consuming, as it requires a physical charging point and an electric vehicle to connect and disconnect as needed. This simulation platform replicates a charging station environment, allowing seamless testing and interaction without the need for physical hardware.

Key Features
Docker: All services are containerized to ensure consistency across different environments.
Microservices Architecture: Services are modular and independent, allowing scalability and easy maintenance.
Keycloak: Used for user authentication and authorization, providing secure access management.
Layered Architecture: Ensures separation of concerns for better maintainability.
Swagger: API documentation for easy testing and interaction with the services.
Config Server: Centralized configuration management for services.
Discovery Server: Enables dynamic service discovery.
WebSocket: Provides real-time communication capabilities.
Liquibase: Database migration and versioning tool for schema management.
OCPP Protocol: Implements the OCPP protocol for effective communication with the simulated charging stations.
Project Goals
This project aims to create a simulation of an OCPP-based charging station management system, which can:

Replace the need for physical charging stations and electric vehicles during testing phases.
Reduce the costs and time involved in setting up and running test environments for OCPP-compliant systems.
Allow developers and testers to easily simulate charging point interactions, including connection, disconnection, and data exchange.
Technology Stack
Docker: To containerize services and ensure easy setup and deployment.
Spring Boot Microservices: For building individual services with a clear separation of responsibilities.
Keycloak: For managing security and access control in the simulation environment.
Spring Cloud Config: Manages external configuration across services in a centralized way.
Spring Cloud Netflix Eureka (Discovery Server): Enables service registration and discovery, essential in a microservices architecture.
WebSocket: For real-time communication with the simulated charging stations.
Liquibase: Manages database migrations.
OCPP: The core protocol used for simulating communication between charging points and the central system.
Installation and Setup
Clone this repository:

bash
Kodu kopyala
git clone https://github.com/your-username/charging-station-simulation.git
cd charging-station-simulation
Set up environment variables in a .env file as needed.

Start the Docker containers:

bash
Kodu kopyala
docker-compose up
Access the services via the specified ports (refer to the documentation or docker-compose.yml for more details).

Usage
Swagger: Visit http://localhost:{port}/swagger-ui.html to explore API endpoints for each service.
Keycloak: Access the Keycloak admin console to manage users and roles.
Simulating Charging Stations: Use WebSocket connections to simulate charging point interactions, such as establishing and terminating connections, sending meter values, and handling charging sessions.
Contributing
Fork this repository.
Create a feature branch (git checkout -b feature/YourFeature).
Commit your changes (git commit -m 'Add YourFeature').
Push to the branch (git push origin feature/YourFeature).
Open a Pull Request.
License
This project is licensed under the MIT License. See the LICENSE file for details.
