# sponsored-ads-project

## Overview

The online-sponsored-ads System is a web application designed to manage and serve advertisements. It handles campaigns, products, and categories with a focus on serving relevant ads.

## Entity Relationships

### Campaign Entity

- **name**: Campaign name
- **startDate**: Start date of the campaign
- **bid**: Bid amount
- **isActive**: Campaign status
- **products**: Associated products

### Product Entity

- **title**: Product title
- **price**: Product price
- **serialNumber**: Serial number
- **campaigns**: Associated campaigns
- **categories**: Associated categories

### Category Entity

- **name**: Category name
- **products**: Associated products

## Swagger UI
Open your browser at the following URL for Swagger UI (giving REST interface details):
http://localhost:8080/swagger-ui/index.html#/ads-controller

## Initial Data
There is a seed data file located at  ```src/main/resources/data.sql``` .
You can modify this file according to your specific requirements and data specifications.

## Application Layers

1. **Controller Layer**: Handles HTTP requests and responses.
2. **Service Layer**: Contains business logic and interacts with repositories.
3. **Repository Layer**: Manages interactions with the database.
4. **Model Layer**: Defines entities and data structures.
5. **DTO Layer**: Defines data structures for communication.
6. **Exception Layer**: Custom exceptions for error handling and Centralized Error Handling for the application.
7. **Util Layer**: Utility classes for additional functionality.

## How to Run Using Docker Compose

Follow these steps to run the application using Docker Compose:

1. **Clone the Repository:**

   ```bash
   git clone https://github.com//eliyaaki/sponsored-ads-project.git
   cd sponsored-ads-project
  
Run this command to start the applications.

	$ docker-compose up -d
  

## How to run the application without Docker

### Building the project
Clone the project and use Maven to build the server

	$ mvn clean package
  
