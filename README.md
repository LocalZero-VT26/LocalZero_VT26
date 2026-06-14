# LocalZero_VT26
LocalZero is a web app for local sustainability work. Users can join environmental initiatives, post updates, chat with other members, log eco actions, and receive in-app notifications.


## Tech stack

- **Frontend:** React, Vite
- **Backend:** Spring Boot, Spring Security, Spring Data JPA
- **Database:** PostgreSQL
- **Auth:** JWT

## Requirements

- Java 21
- Maven
- Node.js 20+
- PostgreSQL

## Setup

1. Clone the repository
2. Create a PostgreSQL database
3. Create a `.env` file in the project root:

```env backend
DB_URL=jdbc:postgresql://localhost:5432/localzero
DB_USER=your_username
DB_PASS=your_password
JWT_SECRET=your_secret_key
```

```env frontend
VITE_API_BASE_URL=
```

4. Start the backend:

```bash
./mvnw spring-boot:run
```

On Windows:

```bash
.\mvnw.cmd spring-boot:run
```

5. Start the frontend:

```bash
cd frontend
npm install
npm run dev
```

- Backend: http://localhost:8080  
- Frontend: http://localhost:5173


- Email: `admin@localzero.se`
- Password: `localzero-admin`

You can change these in the `application.properties` file.
