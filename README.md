# OrderNest 🍔

A scalable food ordering backend built with Java 21, Spring Boot 3.x, and a microservices architecture.

---

## Architecture Overview

```
                          ┌─────────────────────┐
                          │     API Gateway      │
                          │ (Spring Cloud Gateway│
                          │  + JWT Validation)   │
                          └──────────┬──────────┘
                                     │
          ┌──────────────────────────┼────────────────────────────┐
          │                          │                            │
   ┌──────▼──────┐          ┌────────▼──────┐           ┌────────▼──────┐
   │ User & Auth │          │  Order Service│           │  Restaurant   │
   │  Service    │          │               │           │  & Menu Svc   │
   └─────────────┘          └───────┬───────┘           └───────────────┘
                                    │ Kafka
          ┌─────────────────────────┼────────────────────────────┐
          │                         │                            │
   ┌──────▼──────┐          ┌───────▼──────┐           ┌────────▼──────┐
   │  Delivery   │          │   Payment    │           │ Notification  │
   │  Service    │          │   Service    │           │   Service     │
   └─────────────┘          └──────────────┘           └───────────────┘
          │                         │
   ┌──────▼──────┐          ┌───────▼──────┐
   │  Analytics  │          │    Admin     │
   │  Service    │          │  Dashboard   │
   └─────────────┘          └──────────────┘
          │
   ┌──────▼──────┐
   │  Review &   │
   │  Rating Svc │
   └─────────────┘
```

---

## Services

| Service | Owner | Port | Description |
|---|---|---|---|
| `eureka-server` | Dev 2 | 8761 | Service discovery |
| `api-gateway` | Dev 2 | 8080 | Gateway, routing, JWT validation, rate limiting |
| `user-service` | Dev 1 | 8081 | Registration, login, JWT issuance |
| `order-service` | Dev 1 | 8082 | Place & track orders |
| `delivery-service` | Dev 1 | 8083 | Driver management, delivery tracking |
| `restaurant-service` | Dev 2 | 8084 | Restaurant & menu management |
| `payment-service` | Dev 2 | 8085 | Payment processing & refunds |
| `notification-service` | Dev 2 | 8086 | Email/SMS alerts via Kafka |
| `analytics-service` | Dev 3 | 8087 | Real-time metrics, order/revenue analytics |
| `admin-service` | Dev 3 | 8088 | Platform administration, moderation |
| `review-service` | Dev 3 | 8089 | Customer reviews & star ratings |

---

## Tech Stack

- **Language:** Java 21
- **Framework:** Spring Boot 3.x
- **Database:** PostgreSQL
- **Cache:** Redis
- **Messaging:** Apache Kafka
- **Service Discovery:** Netflix Eureka
- **Gateway:** Spring Cloud Gateway
- **Containerisation:** Docker + Docker Compose
- **Tracing:** Micrometer + Zipkin
- **Config:** Spring Cloud Config Server
- **Resilience:** Resilience4j (circuit breaker, retry)

---

## Kafka Topics

| Topic | Producer | Consumers |
|---|---|---|
| `order-placed` | Order Service | Payment, Notification, Analytics, Admin |
| `order-status-updated` | Order Service | Notification |
| `order-cancelled` | Payment Service | Notification |
| `payment-success` | Payment Service | Notification, Analytics |
| `payment-failed` | Payment Service | Notification, Admin |
| `delivery-status-updated` | Delivery Service | Notification, Analytics |
| `review-events` | Review Service | Analytics, Notification |

---

## Developer Assignments

| Dev | Services | Key Phases |
|---|---|---|
| **Dev 1** | User/Auth, Order, Delivery | JWT issuance, Kafka producer, driver assignment |
| **Dev 2** | API Gateway, Eureka, Restaurant, Payment, Notification | Gateway routing, rate limiting, Kafka consumer |
| **Dev 3** | Analytics, Admin Dashboard, Review & Rating | Metrics aggregation, admin moderation, review lifecycle |

See individual prompt files for full phase plans:
- [`DEV1_PROMPT.txt`](./DEV1_PROMPT.txt)
- [`DEV2_PROMPT.txt`](./DEV2_PROMPT.txt)
- [`DEV3_PROMPT.txt`](./DEV3_PROMPT.txt)

---

## Getting Started

### Prerequisites
- Java 21
- Docker & Docker Compose
- Maven 3.9+

### Run the infrastructure

```bash
docker-compose up -d
```

This starts: PostgreSQL, Redis, Kafka, Zookeeper, Eureka, and Zipkin.

### Run a service

```bash
cd user-service
mvn spring-boot:run
```

---

## Coordination Rules

1. **Never push directly to `main`** — open a PR for every feature.
2. **Shared files** (`docker-compose.yml`, gateway routes) — coordinate in your team channel before editing.
3. **Phase order must be respected** — do not start Phase N+1 until Phase N is complete.
4. **Dev 2 owns Eureka** — all other services must confirm registration before proceeding past Phase 1.
5. **Dev 1 owns JWT structure** — Dev 3 Admin Service depends on role claims; agree on format early.
6. **Commit after every meaningful feature** — small, descriptive commits.

---

## Phase Milestones (shared across all devs)

| Phase | Milestone |
|---|---|
| 1 | All services registered in Eureka, shared Docker Compose running |
| 2 | Auth working end-to-end, Gateway routing all services |
| 3–6 | Core service logic + Kafka flows operational |
| 7 | Resilience patterns in place (circuit breakers, retries, caching) |
| 8 | Distributed tracing + centralised config |
| 9 | Security hardened — JWT at gateway only, service-to-service auth |
| 10 | Full test suite: unit + integration + contract |

---

## Project Structure

```
ordernest/
├── user-service/
├── order-service/
├── delivery-service/
├── restaurant-service/
├── payment-service/
├── notification-service/
├── api-gateway/
├── eureka-server/
├── analytics-service/        ← Dev 3
├── admin-service/            ← Dev 3
├── review-service/           ← Dev 3
├── docker-compose.yml
├── DEV1_PROMPT.txt
├── DEV2_PROMPT.txt
├── DEV3_PROMPT.txt
└── README.md
```
