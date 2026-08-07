# Baseera بصيرة

> Early awareness, gentle guidance. A platform that helps parents understand, support, and celebrate their child's development — built for families of children with ASD and ADHD.

Baseera lets a parent describe what they've noticed about their child in their own words, get AI-guided early insight (never a diagnosis), track progress over time, discover matched activities, find nearby specialist centers, and keep every report safely organized — all in a bilingual (English/Arabic, full RTL) experience.

---

## Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
    - [1. Clone the repository](#1-clone-the-repository)
    - [2. Backend setup](#2-backend-setup)
    - [3. Frontend setup](#3-frontend-setup)
    - [4. Run the platform](#4-run-the-platform)
- [Environment Variables](#environment-variables)
- [Default Ports](#default-ports)
- [Design System](#design-system)
- [Testing the API Directly](#testing-the-api-directly)
- [Contributing](#contributing)
- [Team](#team)
- [License](#license)

---

## Features

| Area | What it does |
|---|---|
| 🔐 **Auth** | Email/password login + Google OAuth2, JWT-secured sessions |
| 👶 **Child profiles** | Add and switch between multiple children per parent account |
| 🧠 **AI Check-in** | Describe behavior in plain language, get an early-guidance assessment (Spring AI + Gemini) — low/medium/high risk signal, never a diagnosis |
| 💬 **Baseera AI Assistant** | Live chat with an AI assistant scoped strictly to child development, ASD/ADHD, and using the app — declines off-topic questions by design |
| 🎯 **Activities** | Browse activities matched to a child's age and condition; add activities to a child's plan |
| 🏥 **Centers** | Find nearby specialist centers on a map |
| 📁 **Reports / Vault** | Upload medical/developmental reports (PDF), get an AI-generated progress summary and improvement signs per attachment |
| 🛠️ **Admin Dashboard** | Manage activities/centers/accounts, live KPIs, registration trend and account-status charts (Chart.js) |
| 🌐 **Bilingual** | Full English/Arabic support with automatic RTL layout switching |

---

## Tech Stack

**Backend**
- Java, Spring Boot 4.1
- Spring Security (JWT + Google OAuth2)
- Spring Data JPA / Hibernate
- Spring AI (Gemini) — assessment insight + Baseera AI Assistant
- MySQL

**Frontend**
- Angular 21 (standalone components, signals)
- Chart.js (Admin Dashboard)
- Bilingual i18n via a shared `translations.ts` + `Language` signal service

---

## Project Structure

```
Baseera/
├── Baseera/                    # Spring Boot backend
│   ├── src/main/java/com/example/Baseera/
│   │   ├── controller/
│   │   ├── service/
│   │   ├── entity/
│   │   ├── dto/
│   │   ├── repository/
│   │   ├── security/           # JWT filter, OAuth2 config
│   │   └── exception/          # Custom exceptions + GlobalExceptionHandler
│   └── src/main/resources/
│       └── application.properties
│
├── baseera-frontend/            # Angular frontend
│   └── src/app/
│       ├── features/            # welcome, login, register, home, select-child,
│       │                        # assessment, activities, center, vault, admin,
│       │                        # assistant
│       └── shared/
│           ├── components/      # navbar, sidebar, layout, footer
│           ├── services/        # auth, child, assessment, activity, admin, assistant, language
│           └── i18n/
│               └── translations.ts   # single source of truth for all UI strings
│
└── uploads/                     # uploaded report files (gitignored — see below)
```

---

## Prerequisites

Install these before you start:

| Tool | Version | Check with |
|---|---|---|
| Java (JDK) | 21+ | `java -version` |
| Maven | 3.9+ | `mvn -version` |
| Node.js | 20+ | `node -v` |
| npm | 10+ | `npm -v` |
| Angular CLI | 21+ | `ng version` (install: `npm install -g @angular/cli`) |
| MySQL | 8.0+ | `mysql --version` |
| Git | any recent | `git --version` |

You'll also need:
- A **Google Cloud OAuth 2.0 Client ID/Secret** (for "Continue with Google")
- A **Gemini API key** (for the AI check-in and Baseera AI Assistant), via Google AI Studio

---

## Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/<your-org>/Baseera.git
cd Baseera
```

### 2. Backend setup

**a. Create the database**

```sql
CREATE DATABASE baseeradb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

**b. Configure `Baseera/src/main/resources/application.properties`**

```properties
# --- Database ---
spring.datasource.url=jdbc:mysql://localhost:3306/baseeradb
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD

# --- JPA / Hibernate ---
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
spring.jpa.show-sql=false

# --- JWT ---
app.jwt.secret=YOUR_LONG_RANDOM_SECRET
app.jwt.expiration-ms=86400000

# --- Google OAuth2 ---
spring.security.oauth2.client.registration.google.client-id=YOUR_GOOGLE_CLIENT_ID
spring.security.oauth2.client.registration.google.client-secret=YOUR_GOOGLE_CLIENT_SECRET

# --- Spring AI / Gemini ---
spring.ai.google.genai.api-key=YOUR_GEMINI_API_KEY

# --- File uploads ---
app.upload.dir=./uploads
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

> Never commit real secrets here — see [Environment Variables](#environment-variables) for the recommended approach.

**c. Install & run**

```bash
cd Baseera
mvn clean install
mvn spring-boot:run
```

The backend starts on **http://localhost:8080**.

### 3. Frontend setup

```bash
cd baseera-frontend
npm install
```

Confirm `src/environments/environment.ts` (or wherever your API base URL lives) points at `http://localhost:8080`.

### 4. Run the platform

Backend and frontend run separately, in two terminals:

```bash
# Terminal 1 — backend
cd Baseera
mvn spring-boot:run

# Terminal 2 — frontend
cd baseera-frontend
ng serve
```

Open **http://localhost:4200** — register an account, add a child, and try a check-in.

---

## Environment Variables

For anything beyond local testing, don't hardcode secrets in `application.properties`. Use environment variables instead and reference them:

```properties
spring.datasource.password=${DB_PASSWORD}
app.jwt.secret=${JWT_SECRET}
spring.security.oauth2.client.registration.google.client-id=${GOOGLE_CLIENT_ID}
spring.security.oauth2.client.registration.google.client-secret=${GOOGLE_CLIENT_SECRET}
spring.ai.google.genai.api-key=${GEMINI_API_KEY}
```

Then set them in your shell / IDE run configuration / `.env` (never committed):

```
DB_PASSWORD=...
JWT_SECRET=...
GOOGLE_CLIENT_ID=...
GOOGLE_CLIENT_SECRET=...
GEMINI_API_KEY=...
```

---

## Default Ports

| Service | URL |
|---|---|
| Frontend (Angular) | http://localhost:4200 |
| Backend (Spring Boot) | http://localhost:8080 |
| MySQL | localhost:3306 |

---

## Design System

The frontend follows a single shared design system, defined once in `src/styles.css`:

| Token | Value |
|---|---|
| Primary | `#3562E9` |
| Primary hover | `#4A76F5` |
| Secondary (teal) | `#14B8A6` |
| Text | `#16213E` |
| Muted text | `#64708A` |
| Background | `#F8FAFC` |
| Heading font | Quicksand |
| Body font | Nunito Sans |
| Arabic font | Tajawal |
| Card radius | 16px |
| Button/input radius | 9999px (pill) |

Every page consumes these via CSS custom properties (`var(--color-primary)`, etc.) rather than hardcoding hex values — if you're adding a new page, do the same.

---

## Testing the API Directly

A Postman collection is available for exercising the Baseera AI Assistant endpoint (login → English chat → Arabic chat → off-topic rejection → unauthenticated check) — ask a maintainer for `baseera-assistant.postman_collection.json`, or build requests directly against:

```
POST http://localhost:8080/api/auth/login
POST http://localhost:8080/api/assistant/chat
```

---

## Contributing

1. Create a feature branch: `git checkout -b feature/your-feature-name`
2. Keep frontend strings in `shared/i18n/translations.ts` — never hardcode UI text in a component
3. Follow the existing design tokens (see [Design System](#design-system)) rather than introducing new colors/fonts
4. Open a pull request with a clear description of what changed and why

---

## Team

Built by **Munee**, **Walaa**, and **Shahd** as a final project for the **TRA Nafath Program**, via **Codeline**.

---

## License

This project is for educational purposes as part of the TRA Nafath training program. Add a license here if the team decides to open-source it.