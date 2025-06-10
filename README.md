# 📄 DocStream - Reactive Document Processing System

**DocStream** is a reactive and distributed document processing system developed as a backend portfolio project. It processes documents through multiple stages — OCR, parsing, validation, and storage — using decoupled microservices communicating via Kafka. The entire environment runs locally via Docker Compose and includes full observability with Prometheus and Grafana.

---

## 🚀 Technologies

- **Java 21 + Virtual Threads (Project Loom)**
- **Spring Boot 3 + Spring WebFlux**
- **Apache Kafka (asynchronous messaging)**
- **PostgreSQL + R2DBC (reactive storage)**
- **Tesseract OCR (text recognition in images)**
- **Prometheus + Grafana (observability)**
- **Docker + Docker Compose (fully local, no cloud costs)**
- **Lombok, MapStruct, OpenAPI, JUnit 5, Testcontainers**

---

## ⚙️ Architecture

```txt
┌────────────┐    ┌────────────┐    ┌─────────────┐    ┌─────────────────┐
│ OCRService │──▶│ ParserSvc  │──▶│ ValidatorSvc │──▶│ StorageService   │
└────┬───────┘    └────┬───────┘    └────┬────────┘    └────────┬────────┘
     │                 │                │                      │
     ▼                 ▼                ▼                      ▼
   Kafka            Kafka            Kafka                  PostgreSQL
 Topics:         Topics:         Topics:                  
 raw-docs     parsed-docs     validated-docs             

```

---

## ▶️ Running Locally

- git clone https://github.com/your-username/docstream-backend.git
- cd docstream-backend
- docker-compose up --build

**Access dashboards:**

- **Grafana**: http://localhost:3000 (admin/admin)
- **Prometheus**: http://localhost:9090
- **PostgreSQL**: localhost:5432 (user: user, password: password)

---

## 🧠 Completed to date

As of today, the project contains:
- **Text to image recognition**
- **Parts of code with reactive programming**
- **Communication between microservices based on Kafka's events**

---

## ⏰ Future Improvements

- **Virtual threads**
- **Event retries using CircuitBreaker**
- **Recognition and management of different types of images (invoices, payroll, etc.)**
- **Data storage in PostgreSQL (or MongoDB)**
---