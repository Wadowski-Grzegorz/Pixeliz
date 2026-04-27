# Pixeliz

Web application for creating drawings in pixel art format. Users can collaborate on a drawing using rank-based access control.

## Demo
https://github.com/user-attachments/assets/6a5ef341-bfb9-41fe-ad05-0869b84f9649

## Functionalities
- Create drawings as a guest user.
- Create, store and collaborate with other users on drawings as an authenticated user.
- User statistics collected asynchronously using RabbitMQ events.
- Secure communication using JWT authentication.


## Technologies

**React** - Dynamic UI based on component structure.

**Spring Boot** - REST API with layered architecture for backend logic.

**PostgreSQL** - Relational database with ORM support.

**JWT** - Authentication and authorization for secure communication.

**RabbitMQ** - Asynchronous processing of user events.

## System architecture

<img width="1299" height="455" alt="architecture" src="https://github.com/user-attachments/assets/edff3835-5d61-47ca-8781-95737731226f" />
<br>

Client-server architecture with a layered backend.
- Frontend (React) - user interface for pixel art editor. Communicates with the backend via REST API.
- Backend (Spring Boot) - layered architecture (Controller-Service-Repository). Stateless authentication using JWT (HS256).
- Database (PostgreSQL) - persistent storage for application data, accessed via the repository layer using JPA.
- Message Broker (RabbitMQ) - enables asynchronous, event-driven processing of user activities.


## Install and Run

All steps should be executed from the root directory of the repository.

### Infrastructure
**Requirements:**
    - Docker

```bash
docker compose up --build -d
```

### Backend
**Requirements:**
    - at least Java 17
    - Maven

```bash
cd backend
./mvnw spring-boot:run
```

### Frontend
**Requirements:**
    - Node.js
    - npm

```bash
cd frontend
npm install
npm run dev
```

Open in web browser: http://localhost:5173

## Database schema

![database_schema](https://github.com/user-attachments/assets/61fde1ba-5341-48a6-b2f3-04529cccae60)
Database schema designed for storing drawings with multi-user access.
