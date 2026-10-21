# ADR-0001: Initial Persistence Strategy

## Status

Accepted

## Context

Business Management Platform is a modular monolith backed by PostgreSQL. Its initial domain model includes `Organization`, `Product`, and `Warehouse`, implemented as framework-independent Java classes with explicit identity and domain invariants.

The application needs a consistent way to persist and retrieve these objects without letting persistence concerns dictate business rules. Future capabilities will include transactional inventory and order workflows, tenant-owned data, and operations that require explicit SQL and concurrency control.

The database schema is managed by versioned Flyway migrations. The persistence approach must work with that schema ownership model, support Spring-managed transactions, remain testable against PostgreSQL, and allow SQL behavior to be inspected and optimized when needed.

## Decision

Use **Jakarta Persistence (JPA), implemented by Hibernate, with Spring Data JPA** as the default persistence approach for business entities.

Keep **domain models separate from JPA persistence entities**:

- Domain classes express business concepts, identities, and invariants without JPA annotations or ORM-specific requirements.
- JPA entity classes represent the persistence mapping to PostgreSQL tables.
- Persistence adapters map between domain objects and JPA entities. Begin with explicit mapping code; introduce a mapping library only if real complexity justifies it.
- Application services define transaction boundaries for business operations involving one or more persistence operations.

Retain **Spring JDBC and explicitly written SQL** as available options for queries or operations where SQL-level control, performance, bulk processing, or concurrency requirements justify them. Their use should be based on a concrete need rather than introduced everywhere by default.

Continue to use **Flyway as the authority for database schema changes**. Hibernate must not be used to create or update the schema automatically. Database constraints remain responsible for protecting stored data, while domain invariants protect valid business objects. Equivalent rules across these boundaries must be kept consistent.

## Alternatives Considered

### Plain JDBC

**Advantages:** Direct control of SQL, JDBC operations, and result mapping; minimal persistence abstraction.

**Disadvantages:** Repeated connection, statement, resource-handling, and mapping code for routine entity operations. The project already has JDBC connectivity through Spring, but plain JDBC would add unnecessary boilerplate as the number of entities grows.

**Outcome:** Not selected as the default approach.

### Spring JDBC / JdbcTemplate as the default

**Advantages:** Explicit SQL and predictable queries, with Spring handling common JDBC resource-management tasks and integrating with transactions.

**Disadvantages:** Manual mapping and SQL maintenance for routine persistence operations across a growing domain model.

**Outcome:** Retained for specific use cases, but not selected as the default.

### JPA / Hibernate using the domain classes directly as JPA entities

**Advantages:** Fewer classes and mappings; straightforward use of ORM persistence features.

**Disadvantages:** Existing domain classes would need changes to satisfy JPA requirements, including non-final persistent fields and an appropriate no-argument constructor. Persistence concerns would become coupled to domain modeling choices.

**Outcome:** Not selected initially because preserving the existing framework-independent domain boundary is a deliberate design goal.

### JPA / Hibernate with separate domain and persistence models

**Advantages:** ORM support for routine persistence while preserving domain invariants and keeping ORM concerns out of domain classes. Storage representation can evolve without automatically changing the domain API.

**Disadvantages:** Additional entity classes, mapping code, and consistency checks between representations.

**Outcome:** Selected as the initial strategy; its added complexity will be reviewed as real capabilities are implemented.

## Consequences

### Positive

- Routine entity persistence can use Spring Data JPA while keeping the domain model independent of Hibernate.
- Domain constructors and methods remain the primary entry point for enforcing business invariants.
- Flyway migrations and PostgreSQL constraints remain explicit and reviewable.
- Complex SQL, locking, and performance-sensitive operations can be handled directly when justified.

### Trade-offs and Risks

- Domain-to-persistence mapping introduces additional code and possible mapping defects.
- Developers must understand JPA entity states, persistence contexts, dirty checking, flushing, and transaction boundaries; a repository `save()` call does not always correspond to an immediate SQL statement.
- Lazy loading, unexpected queries, N+1 query patterns, and transaction scope can create correctness or performance problems if not reviewed.
- Validation semantics in Java and PostgreSQL can differ; matching constraints need explicit verification.

### Verification and Follow-up

- Introduce JPA dependencies and implement the first persistence adapter as part of **BMP-014**, not this decision task.
- Confirm that the existing `Organization` domain invariants remain intact when mapped to and from a JPA entity.
- Add meaningful PostgreSQL-backed integration tests for persistence behavior when the relevant task calls for them; do not substitute an in-memory database for PostgreSQL-specific behavior.
- Review generated SQL, transaction behavior, schema validation, and any additional mapping complexity during implementation.
- Revisit this ADR if the separation of models becomes disproportionate to its value or if specific business workflows require a different persistence approach.
