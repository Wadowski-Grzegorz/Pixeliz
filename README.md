# Pixeliz

Web application for creating drawings in pixel art format. Users can collaborate on a drawing using rank-based access control.

## Functionalities
- Create drawing as not logged in user
- Create, store and collaborate on drawing as logged in user
- Statistics per user collected with RabbitMQ
- Safe communication with JWT


## Technologies

**React** Dynamic, fast, up-to-date library. Easy to write and manage which makes it possible for future scability.
**Spring boot** Good for REST API with separete layers which improve clear division of duties and manage application.
**PostgreSQL** relational databse which good compability with ORM

## System architecture

![Diagram bez tytułu](https://github.com/user-attachments/assets/c01fb7bd-14f4-4279-971e-e56892293c86)

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
mvnw.cmd spring-boot:run
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
