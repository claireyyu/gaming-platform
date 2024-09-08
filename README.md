# High-Performance Online Gaming Platform Backend

This project is a high-performance backend system for an online gaming platform, built using the Spring Boot framework. It supports key functionalities such as user authentication, game data processing, transaction management, and more. The system is optimized for efficient data handling, secure user management, and fast resource loading using MySQL, Redis, and Webpack.

## Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Deployment Guide](#deployment-guide)
- [System Architecture](#system-architecture)

## Features

- **User Authentication & Authorization**: Implements JWT-based token authentication with support for multiple login methods and distributed session management using Redis for session sharing across clusters.
- **Game Data Processing**: Efficient game data processing with persistent storage of user data, dynamic resource loading, and real-time updates.
- **Transaction Management**: Manages in-game transactions, including recharge systems and transaction records.
- **User Management & Game Management**: Complete management modules for users and game data, including features like leaderboards and social interactions.
- **Optimized Performance**:
  - **MySQL Partitioning & Sharding**: Implements database partitioning and sharding to handle large-scale user data, game resources, and transaction records efficiently.
  - **Redis Caching**: Caches user information, game configurations, and leaderboard data to reduce the load on the database and improve query performance.
  - **Webpack for Frontend Optimization**: Bundles and compresses frontend static resources, reducing file sizes and accelerating resource loading. HTTP cache headers are set to optimize network requests.

## Tech Stack

- **Backend**: Spring Boot, MySQL, Redis
- **Frontend**: Bundled and optimized using Webpack
- **Security**: JWT-based token authentication, double MD5 hashing for passwords, Redis for session management
- **Database Optimization**: MySQL partitioning and sharding, Redis caching with TTL (Time to Live)

### Prerequisites
- Java 8
- Maven
- MySQL
- Redis
- Node.js and Webpack (for bundling frontend resources)

## Deployment Guide

1. **Prepare the Environment**:  
   - Install a virtual machine environment like VMware.
   - Set up an Ubuntu instance on VMware.
   - Install and configure Java 8, Tomcat, MySQL, and Redis on Ubuntu.

2. **Package the Application**:  
   - In your local development environment (e.g., IntelliJ IDEA), package the project into a `.war` file by running `mvn package`. This will generate the `shop.war` file in the `target` directory.

3. **Set Up the Database**:  
   - Log in to the MySQL database and run the schema scripts to create the required database and tables:
     ```sql
     source ./schema.sql;
     source ./dbdata.sql;
     ```

4. **Deploy the WAR File**:  
   - Transfer the `shop.war` file to the `webapps` directory of the Tomcat server on your Ubuntu virtual machine.

5. **Configure Tomcat**:  
   - Modify the `server.xml` file located in the `conf` directory of Tomcat to ensure the correct project path:
     ```xml
     <Context docBase="shop" path="/" reloadable="false" />
     ```

6. **Start the Application**:  
   - Start Tomcat using the following command:
     ```bash
     sh startup.sh
     ```

7. **Access the Application**:  
   - Open your browser and visit the application at `http://127.0.0.1:8080/shop`.

## System Architecture

The project is built with a microservice-oriented architecture using the following key components:

- **Spring Boot**: Core backend framework.
- **MySQL**: Database for storing user data, game resources, and transaction records with partitioning and sharding.
- **Redis**: In-memory data store for caching and session management.
- **JWT**: Token-based authentication for securing user sessions.
- **Webpack**: Bundling and compressing frontend static resources to improve loading times.
