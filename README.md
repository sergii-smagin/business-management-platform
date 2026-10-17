# Business Management Platform

Business Management Platform is a multi-tenant business operations platform for managing products, warehouses, inventory, sales, purchasing, billing, users, audit history, and operational reporting.

The system is designed around production-oriented backend concerns such as transactional consistency, concurrency, tenant isolation, security, reliable asynchronous processing, observability, and cloud deployment.

## Overview

The core domain centers on the flow between products, warehouses, inventory, sales orders, and purchasing/receiving.

```text
Products
   ↓
Warehouses
   ↓
Inventory
   ↕
Sales Orders
   ↕
Purchasing / Receiving
```

This domain creates realistic engineering scenarios involving stock reservations, concurrent updates, state transitions, transaction boundaries, idempotency, failure handling, and traceability.

The platform is intentionally narrower than a full ERP system. Engineering depth around the core operational workflows takes priority over adding large numbers of shallow business modules.

## Core Capabilities

The platform is designed to support:

- organizations and tenant boundaries;
- users, roles, and permissions;
- products and SKUs;
- warehouses;
- inventory movements and stock reservations;
- customers and suppliers;
- sales orders and order lifecycle;
- purchasing and receiving;
- limited invoicing and payments;
- audit history;
- operational reporting and analytics.

Capabilities are introduced incrementally as the system evolves.

## Architecture

The initial architecture is a modular monolith built with Java and Spring Boot.

```text
Clients
   |
   v
REST API
   |
   v
Spring Boot modular monolith
   |
   +-- Organization
   +-- Identity
   +-- Product
   +-- Warehouse
   +-- Inventory
   +-- Sales
   +-- Purchasing
   +-- Billing
   +-- Reporting
   |
   v
PostgreSQL
```

The codebase is organized primarily by business capability rather than by global technical layers.

Distributed components are introduced only when they provide a meaningful architectural or operational benefit. Core transactional workflows remain synchronous where atomic consistency is required, while asynchronous processing can later be introduced for notifications, integrations, analytics, and other side effects.

## Engineering Focus

The project emphasizes practical backend engineering problems, including:

- domain modeling and explicit business operations;
- relational data modeling and SQL;
- schema evolution and database migrations;
- transaction boundaries and data integrity;
- locking, isolation, and concurrent operations;
- REST API design, validation, and consistent errors;
- authentication, authorization, and tenant isolation;
- unit, integration, API, concurrency, and failure-path testing;
- idempotency and reliable event processing;
- caching and measurable performance optimization;
- structured logging, metrics, and distributed tracing;
- CI/CD and reproducible infrastructure;
- cloud deployment and operational troubleshooting.

## Technology Direction

### Foundation

The initial technology foundation includes:

- Java;
- Spring Boot;
- Maven;
- HTTP / REST;
- PostgreSQL;
- Flyway;
- JUnit and Spring testing;
- Git / GitHub;
- Docker / Docker Compose.

Persistence abstractions are selected deliberately as real persistence requirements emerge rather than being fixed prematurely.

### Planned Evolution

The platform is expected to create meaningful use cases for technologies and practices such as:

- JPA / Hibernate where appropriate;
- Testcontainers;
- Spring Security;
- OAuth 2.0 / OpenID Connect where appropriate;
- Redis;
- Kafka;
- transactional outbox and idempotent consumers;
- GitHub Actions;
- Spring Boot Actuator;
- Prometheus;
- Grafana;
- OpenTelemetry;
- AWS;
- Terraform;
- Kubernetes;
- AI / LLM integration through controlled application capabilities.

Technology choices are driven by concrete engineering problems and architectural value rather than by technology count alone.

## Roadmap

The roadmap is milestone-based rather than calendar-based.

1. **Development Foundation** — repository, Java, IDE, Spring Boot, Docker, PostgreSQL, and initial testing.
2. **Core Business Foundation** — organizations, products, warehouses, persistence, migrations, APIs, and integration testing.
3. **Inventory and Concurrency** — inventory movements, reservations, transaction isolation, locking, and overselling prevention.
4. **Sales and Purchasing Workflows** — sales orders, purchase orders, state transitions, receiving, and inventory integration.
5. **Security and Multi-Tenancy** — authentication, authorization, roles, tenant isolation, and security testing.
6. **Production Hardening** — stronger testing, API documentation, performance work, health checks, logging, and CI/CD.
7. **Caching and Distributed Processing** — Redis, Kafka, event delivery, retries, idempotency, and eventual consistency.
8. **Observability** — metrics, dashboards, OpenTelemetry, tracing, and cross-component diagnostics.
9. **Cloud and Infrastructure** — AWS, deployment automation, secrets, and Terraform.
10. **Advanced Deployment** — Kubernetes and operational deployment practices.
11. **AI-Assisted Business Capabilities** — controlled LLM tools, structured output, evaluation, and AI observability.

## Local PostgreSQL Setup

The project provides a PostgreSQL 18 development database through Docker Compose. The Spring Boot application connects to this database using the `local` profile.

**Prerequisite:** Docker Desktop (or Docker Engine) with Docker Compose.

From the repository root, create your local environment file in PowerShell:

```powershell
Copy-Item .env.example .env
```

Set `BMP_DB_PASSWORD` in `.env` to a non-empty password for local development.
Do not commit `.env` to Git.

Start PostgreSQL:

```powershell
docker compose up -d db
```

Connection details for a local SQL client such as DBeaver:

| Setting | Value |
|---|---|
| Host | `127.0.0.1` |
| Port | `5432` |
| Database | `bmp` |
| User | `bmp` |
| Password | Value of `BMP_DB_PASSWORD` in `.env` |

### Run Spring Boot with PostgreSQL

The `local` Spring profile loads `src/main/resources/application-local.properties`, which configures JDBC to connect to `jdbc:postgresql://localhost:5432/bmp` as user `bmp`. The database password is resolved from the `BMP_DB_PASSWORD` environment variable; it is not stored in the application configuration.

**Important:** Docker Compose reads `.env` when starting the database, but launching Spring Boot separately does not automatically load that file. The Spring Boot process must receive `BMP_DB_PASSWORD` as an environment variable, using the same password as in `.env`.

In IntelliJ IDEA, open the application Run Configuration and set:

- **Active profiles:** `local` (alternatively, add `--spring.profiles.active=local` to Program arguments).
- **Environment variables:** `BMP_DB_PASSWORD` with the password from the local `.env` file.

Then run `BusinessManagementPlatformApplication`.

Alternatively, from PowerShell, after making `BMP_DB_PASSWORD` available in the current shell's environment, run:

```powershell
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=local"
```

The application starts on `http://localhost:8080`. With Flyway enabled, a successful `local` startup also confirms that Flyway could connect to PostgreSQL and validate or apply the pending migrations. Database-backed application operations should still be tested separately when implemented.

### Database Migrations

Database schema changes are managed by Flyway using version-controlled SQL migration files in `src/main/resources/db/migration`.

When Spring Boot starts with the `local` profile, Flyway validates the existing migration history and applies pending migrations in version order before the application begins serving HTTP requests.

The initial migrations are:

- `V1__create_organizations.sql` — creates the `organizations` table with its primary key and name constraints.
- `V2__create_warehouses.sql` — creates the `warehouses` table with its primary key, name constraints, and foreign key to `organizations`.

Flyway records applied migrations, versions, checksums, and outcomes in `public.flyway_schema_history`. Already-applied versioned migrations are not executed again on subsequent starts. Do not edit an applied versioned migration; create a new versioned SQL file for any subsequent schema change.

The PostgreSQL database itself is initialized by Docker Compose on first setup. Flyway manages the schema objects within that database.

### Stop PostgreSQL

Check the service status with `docker compose ps`.
Stop and remove the Compose containers and network without deleting the database volume:

```powershell
docker compose down
```

PostgreSQL data is kept in a named Docker volume and persists across container recreation.
Avoid `docker compose down -v` unless you explicitly intend to remove the project's database data.

## Documentation

Repository documentation is limited to information that belongs with the software itself.

- `README.md` — public system overview, architecture direction, and development roadmap.
- `docs/adr/` — Architecture Decision Records for significant technical decisions, created when such decisions are actually made.
- Additional technical documentation may be added under `docs/` as the implementation grows.

Internal planning, learning workflow, and task-tracking documents are maintained separately from the public repository.
