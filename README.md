# 🔗 URL Shortener

A cloud-native URL shortening service built with **Java 21**, **Spring Boot** and **AWS DynamoDB**. The application allows authenticated users to create custom or random short links, redirect users, track click analytics and manage their URLs using a scalable serverless architecture.

---

## 🚀 Features

* 🔐 JWT Authentication (RSA Key Pair)
* 🔗 Create short URLs with custom or random slugs
* 📈 Real-time click analytics
* ⚡ Atomic counter using DynamoDB
* 👤 Multi-user support
* ☁️ AWS DynamoDB integration
* 🐳 Local development with Docker & LocalStack
* 📅 Link expiration support
* 🏗️ Clean Architecture

---

## 🛠️ Tech Stack

* Java 21
* Spring Boot 3
* Spring Security
* AWS DynamoDB
* AWS SDK v2 (Enhanced Client)
* Docker
* LocalStack
* JWT (RSA)
* Maven

---

## 📂 Project Structure

```text
src
├── application
├── domain
├── infrastructure
├── presentation
└── shared
```

The project follows **Clean Architecture**, separating business rules from infrastructure concerns.

---

## 📋 Prerequisites

Before running the project, make sure you have installed:

* JDK 21+
* Maven 3+
* Docker
* Docker Compose
* AWS CLI (optional)

---

## 🔐 Generate RSA Keys

Generate the JWT key pair inside:

```bash
src/main/resources
```

Run:

```bash
openssl genrsa -out app.key 2048
openssl rsa -in app.key -pubout -out app.pub
```

---

## ▶️ Running the Project

Clone the repository:

```bash
git clone https://github.com/Murilo-Salesse/encurtador-de-links.git
```

Enter the project:

```bash
cd encurtador-de-links
```

Install dependencies:

```bash
mvn clean install
```

Start LocalStack (or Docker services):

```bash
docker compose up -d
```

Run the application:

```bash
mvn spring-boot:run
```

---

## 📊 DynamoDB Tables

### `tb_links`

Stores the shortened URLs.

Example:

```json
{
  "link_id": "fbr3",
  "original_url": "https://google.com",
  "user_id": "5d3bff48-4a9a-447f-8bf6-8bab3918e422",
  "clicks": 12,
  "created_at": "2026-06-26T12:30:00"
}
```

---

### `tb_links_analytics`

Stores analytics for each user.

Partition Key:

```text
user_id
```

Sort Key:

```text
link_id
```

Example item:

```json
{
  "user_id": "5d3bff48-4a9a-447f-8bf6-8bab3918e422",
  "link_id": "fbr3"
}
```

---

## 🔑 Authentication

The API uses **JWT Bearer Token**.

Example:

```http
Authorization: Bearer eyJhbGciOiJSUzI1NiJ9...
```

---

## 📌 Main Endpoints

| Method | Endpoint      | Description                     |
| ------ | ------------- | ------------------------------- |
| POST   | `/auth/login` | Authenticate user               |
| POST   | `/links`      | Create a short URL              |
| GET    | `/{slug}`     | Redirect to original URL        |
| GET    | `/links`      | List authenticated user's links |
| DELETE | `/links/{id}` | Delete a short URL              |

---

## 📈 Architecture

```
Client
   │
   ▼
Spring Boot API
   │
   ├── Authentication (JWT)
   ├── Application Layer
   ├── Domain Layer
   └── Infrastructure
           │
           ▼
      DynamoDB
```

---
