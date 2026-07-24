# Setup And Usage

## Prerequisites

- JDK 21 for the Maven domain model.
- Maven for `source/domain-model-maven/carservice-center`.
- NetBeans/Ant-compatible Java EE tooling for the JSP/Servlet projects.
- Apache Derby server configured locally.

## Maven Domain Model

```bash
cd source/domain-model-maven/carservice-center
mvn compile
```

## JSP/Servlet Web Application

```bash
cd source/java-web/WebApplication1
ant
```

Deploy through NetBeans or a compatible Java EE server.

## Database Environment

```bash
CAR_SERVICE_DB_URL=jdbc:derby://localhost:1527/ProjectSoftware
CAR_SERVICE_DB_USER=YOUR_DATABASE_USER_HERE
CAR_SERVICE_DB_PASSWORD=YOUR_DATABASE_PASSWORD_HERE
```

## Review Flow

1. Inspect the requirements and diagram indexes in `docs/`.
2. Review use-case and architecture diagrams in `diagrams/`.
3. Inspect the Java domain model under `source/domain-model-maven`.
4. Inspect JSP/Servlet controllers and views under `source/java-web/WebApplication1`.
5. Review TestNG files as generated testing artifacts, not passing tests.
