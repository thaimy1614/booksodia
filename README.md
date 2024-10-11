# Booksodia - E-Commerce & Social Media Platform

This repository contains a microservices-based platform that combines e-commerce features with a social media experience. Users can not only browse and purchase products but also interact with others by creating posts, exchanging messages, and sharing content.

# Table of Contents

	Project Overview
	Microservices Overview
	Architecture
	Prerequisites
	Running the Application
	Technologies Used
	License

# Project Overview
This project demonstrates an application where users can both shop for products and engage with a social media platform. The system is built using microservices architecture for scalability and flexibility. It includes services for handling product management, order processing, user profiles, social media posts, and notifications.

# Microservices Overview
The application consists of the following microservices:

	api-gateway: Manages incoming requests and routes them to appropriate services.
	book-service: Manages products (books) in the e-commerce store.
	cart-service: Handles user shopping carts and product selection.
	custom-config: Centralized configuration management for all services.
	identity-service: Manages user authentication and authorization.
	notification-service: Sends notifications for new posts, comments, and orders.
	order-service: Manages product orders and transactions.
	payment-service: Handles payments and integrates with payment gateways.
	post-service: Social media service where users can create and interact with posts (like, comment, share).
	profile-service: Manages user profiles including personal details and posts.
	search-service: Provides search functionality across the platform (products and posts).
	web-app: The front-end application that integrates both shopping and social media functionalities.
 
# Architecture
This platform is designed using microservices architecture and follows these principles:

	Spring Boot for creating individual services.
	API Gateway to route requests and manage load balancing.
	Asynchronous communication between services using Kafka.
	Database per service: Each microservice has its own database for data isolation and security.
	Event-Driven Architecture: Notifications and data exchange between services using Kafka.
	Docker: Services are containerized for consistent and scalable deployment.
 	Debezium: CDC and stream data from Postgres to Elasticsearch

# Prerequisites
To run this project locally, you will need:

	Java 17 or higher
	Docker and Docker Compose
	Maven for building the services
	Kafka and Zookeeper for messaging (optional)
	PostgreSQL (or your preferred database) for services requiring data persistence

# Running the Application
Follow these steps to run the application:

	Clone the Repository:
		git clone https://github.com/thaimy1614/booksodia.git
		cd booksodia
	Build the Project: Build the services using Maven:
 		mvn clean install
	Run Docker: Start the services using Docker Compose:
 		docker-compose up -d

# Technologies Used

	Java 17
	Spring Boot for building microservices
	Spring Cloud for API Gateway and service discovery
	Kafka for messaging and event-driven architecture
	PostgreSQL for persistence
	Docker for containerization
	React for front-end web application
 	Debezium for capture data changes\
	Swagger for UI api testing
 	Flyway for update models

# License
This project is licensed under the MIT License. See the LICENSE file for more details.
