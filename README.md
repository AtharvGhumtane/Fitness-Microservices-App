# 🏋️ Fitness Microservices

A full-stack fitness activity tracking and AI-powered recommendation system built with **Spring Boot Microservices**, **React**, and **Gemini AI**. Containerized with **Docker Compose** for one-command startup.

---

## 📐 Architecture

```
┌─────────────┐     ┌──────────────┐     ┌─────────────────┐
│   Frontend   │────▶│  API Gateway  │────▶│  User Service   │──▶ PostgreSQL
│  (React/MUI) │     │  (WebFlux)    │     │  (Spring Boot)  │
└─────────────┘     │               │     └─────────────────┘
                    │               │     ┌─────────────────┐
                    │               │────▶│ Activity Service │──▶ MongoDB
                    │               │     │  (Spring Boot)  │──▶ RabbitMQ ──┐
                    │               │     └─────────────────┘              │
                    │               │     ┌─────────────────┐              │
                    │               │────▶│   AI Service     │◀─────────────┘
                    └──────────────┘     │  (Gemini AI)     │──▶ MongoDB
                           │             └─────────────────┘
                    ┌──────────────┐
                    │   Keycloak    │  OAuth2 / PKCE Authentication
                    └──────────────┘
                    ┌──────────────┐     ┌─────────────────┐
                    │    Eureka     │     │  Config Server   │
                    │  (Discovery)  │     │  (Centralized)   │
                    └──────────────┘     └─────────────────┘
```

---

## 🧰 Tech Stack

| Layer | Technology |
|-------|-----------|
| **Frontend** | React 18, Vite, Material-UI (MUI v7), Redux Toolkit |
| **API Gateway** | Spring Cloud Gateway (WebFlux) |
| **Backend Services** | Spring Boot 3.5, Java 21 |
| **Service Discovery** | Netflix Eureka |
| **Config Management** | Spring Cloud Config Server (native) |
| **Authentication** | Keycloak (OAuth2 + PKCE) |
| **Databases** | PostgreSQL 16 (users), MongoDB 7 (activities & recommendations) |
| **Messaging** | RabbitMQ (activity → AI processing pipeline) |
| **AI** | Google Gemini API (fitness recommendations) |
| **Containerization** | Docker, Docker Compose |

---

## 🗂️ Project Structure

```
fitness-microservices/
├── eureka/                  # Service Discovery Server (:8761)
├── configserver/            # Centralized Config Server (:8094)
├── gateway/                 # API Gateway + Keycloak Auth (:8081)
├── userservice/             # User Management + PostgreSQL (:8091)
├── activityservice/         # Activity Tracking + MongoDB (:8092)
├── aiservice/               # AI Recommendations + Gemini (:8093)
├── fitness-app-frontend/    # React SPA (:5173)
├── keycloak/                # Keycloak realm config (auto-import)
├── docker-compose.yml       # Full stack orchestration
├── .env.example             # Environment variable template
└── README.md
```

---

## 🚀 Quick Start (Docker)

### Prerequisites

- [Docker Desktop](https://www.docker.com/products/docker-desktop/) installed and running
- A [Gemini API Key](https://aistudio.google.com/apikey) (free tier works)

### 1. Clone & Configure

```bash
git clone <your-repo-url>
cd fitness-microservices

# Create environment file from template
cp .env.example .env
```

Edit `.env` and add your Gemini API key:
```env
GEMINI_API_KEY=your-actual-gemini-api-key
```

### 2. Start Everything

```bash
docker compose up --build -d
```

> ⏱️ First build takes **5–10 minutes** (Maven + npm downloads). Subsequent starts are much faster.

### 3. Access the Application

| Service | URL | Credentials |
|---------|-----|-------------|
| **Frontend** | http://localhost:5173 | — |
| **Keycloak Admin** | http://localhost:8070 | `admin` / `admin` |
| **Eureka Dashboard** | http://localhost:8761 | — |
| **RabbitMQ Management** | http://localhost:15672 | `guest` / `guest` |

### 4. Demo Login

A demo user is pre-configured:
- **Email**: `demo@fitness.com`
- **Password**: `demo123`

### 5. Shut Down

```bash
docker compose down       # Stop services
docker compose down -v    # Stop + delete all data
```

---

## 🛠️ Local Development (Without Docker)

### Prerequisites

- Java 21
- Maven
- Node.js 20+
- PostgreSQL (port 2526, database: `fitness_user_db`)
- MongoDB (port 27017)
- RabbitMQ (port 5672)
- Keycloak (port 8070, realm: `fitness-oauth2`)

### Start Order

Services must start in this order:

1. **Config Server** → `cd configserver && ./mvnw spring-boot:run`
2. **Eureka** → `cd eureka && ./mvnw spring-boot:run`
3. **User Service** → `cd userservice && ./mvnw spring-boot:run`
4. **Activity Service** → `cd activityservice && ./mvnw spring-boot:run`
5. **AI Service** → `cd aiservice && ./mvnw spring-boot:run`
6. **API Gateway** → `cd gateway && ./mvnw spring-boot:run`
7. **Frontend** → `cd fitness-app-frontend && npm install && npm run dev`

### Environment Variables (Local)

Set these for the AI service:
```bash
export GEMINI_API_URL=https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent
export GEMINI_API_KEY=your-gemini-api-key
```

---

## 🔄 Service Communication Flow

```
User logs in via Keycloak (OAuth2 PKCE)
          │
          ▼
Frontend sends JWT token with each request
          │
          ▼
API Gateway validates JWT, syncs user to User Service
          │
          ▼
Gateway routes request to the appropriate microservice
          │
          ├──▶ POST /api/activities → Activity Service
          │         │
          │         ▼
          │    Saves to MongoDB, publishes to RabbitMQ
          │         │
          │         ▼
          │    AI Service consumes message, calls Gemini AI
          │         │
          │         ▼
          │    Recommendation saved to MongoDB
          │
          ├──▶ GET /api/activities → Activity Service (user's activities)
          │
          └──▶ GET /api/recommendations/activity/{id} → AI Service
```

---

## 📡 API Endpoints

All endpoints are accessed through the API Gateway (`http://localhost:8081`).

### User Service
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/users/register` | Register a new user |
| GET | `/api/users/{userId}` | Get user profile |
| GET | `/api/users/{userId}/validate` | Validate user exists |

### Activity Service
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/activities` | Track a new activity |
| GET | `/api/activities` | Get user's activities (X-User-ID header) |
| GET | `/api/activities/{id}` | Get activity by ID |

### AI Service
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/recommendations/user/{userId}` | Get all recommendations for a user |
| GET | `/api/recommendations/activity/{activityId}` | Get recommendation for an activity |

### Supported Activity Types

`RUNNING` · `WALKING` · `CYCLING` · `SWIMMING` · `WEIGHT_TRAINING` · `YOGA` · `HIIT` · `CARDIO` · `STRETCHING` · `OTHER`

---

## 🐳 Docker Services

| Container | Image | Port |
|-----------|-------|------|
| `fitness-postgres` | `postgres:16-alpine` | 5432 |
| `fitness-mongodb` | `mongo:7` | 27017 |
| `fitness-rabbitmq` | `rabbitmq:3-management-alpine` | 5672, 15672 |
| `fitness-keycloak` | `keycloak:26.0` | 8070 |
| `fitness-config-server` | Custom (Maven build) | 8094 |
| `fitness-eureka` | Custom (Maven build) | 8761 |
| `fitness-user-service` | Custom (Maven build) | 8091 |
| `fitness-activity-service` | Custom (Maven build) | 8092 |
| `fitness-ai-service` | Custom (Maven build) | 8093 |
| `fitness-api-gateway` | Custom (Maven build) | 8081 |
| `fitness-frontend` | Custom (Node + nginx) | 5173 |

---

## 📝 License

This project is for educational and portfolio purposes.
