Fitness Microservices Application
Overview
This project is a fitness activity tracking and recommendation system built using a microservices architecture. It features multiple backend services and a React frontend, providing users with detailed activity tracking, AI-generated fitness recommendations, and secure authentication.

Project Structure
Eureka Service
Service registry and discovery server to manage microservices.

User Service
Manages user data with PostgreSQL and exposes REST endpoints.

Activity Service
Handles activity tracking, MongoDB for activity data storage, and communicates with AI for fitness recommendations.

AI Service
Integrates with Gemini AI to generate personalized fitness advice based on user activities.

Fitness App Frontend
React-based single-page application using Vite, Material-UI (MUI v7), React-Redux, and react-oauth2-code-pkce for authentication.

Key Technologies
Spring Boot (Java) with Webflux and Spring Cloud for microservices architecture.

Eureka Server for service registration and discovery.

PostgreSQL and MongoDB databases.

Java Lombok for boilerplate code reduction.

Gemini AI integration for generating fitness insights.

React, Vite, MUI (Material-UI v7), Redux Toolkit for frontend.

OAuth2 PKCE authentication flow.

RabbitMQ (optional for messaging, configured if used).

Setup Instructions
Backend Services
Setup PostgreSQL (default port 2526) with database fitness_user_db.

Setup MongoDB (default port 27017) for fitness activity data.

Configure application.properties files for all services with correct DB URIs and Eureka URLs.

Start Eureka server (localhost:8083), then start each microservice in sequence.

Ensure AI service is running with access to Gemini API credentials set in properties or environment variables.