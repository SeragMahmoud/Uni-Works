# Car Service Center - Software Engineering 1 and 2

## Overview

Car Service Center is a Software Engineering 1 and 2 case study that connects requirements analysis, UML/design material, Java domain modeling, JSP/Servlet web applications, Derby database access, and TestNG model tests.

## Purpose

The project demonstrates a full academic software engineering lifecycle from analysis and design to implementation and testing.

## Main Features

- Customer registration and login.
- Service browsing.
- Booking workflow.
- Schedule viewing.
- Technician assignment concepts.
- Card and cash payment workflows.
- Review and feedback model concepts.
- Java domain model and Java web application implementations.
- UML, activity, architecture, class, and sequence diagram organization.

## Technology Stack

Java, Maven, Java EE, Servlets, JSP, JDBC, Apache Derby, NetBeans Ant project files, Gson references, TestNG, UML, Draw.io, and Simple UML files.

## Architecture

```text
JSP Views -> Servlet Controllers -> Java Models/dbconnect -> Apache Derby
```

| Folder | Purpose |
| --- | --- |
| `source/domain-model-maven` | Maven Java domain model prototype. |
| `source/java-web` | Main JSP/Servlet web application. |
| `source/java-web-carservice` | Additional Java web implementation. |
| `docs` | Requirements, use-case, testing, and team documentation indexes. |
| `diagrams` | Use-case, activity, architecture, class, and sequence diagrams. |

## Prerequisites

- JDK compatible with the selected project: Java 21 for the Maven prototype, Java 8-era tooling for the NetBeans web projects.
- Maven for the domain-model prototype.
- NetBeans/Ant and a Java EE server such as GlassFish for the JSP/Servlet web projects.
- Apache Derby database configured locally.

## Installation

Maven prototype:

```bash
cd source/domain-model-maven/carservice-center
mvn dependency:resolve
```

Web applications:

Open the NetBeans project folders under `source/java-web/WebApplication1` or `source/java-web-carservice/carservice`.

## Environment Variables

Set these values locally before running database-backed web flows:

| Variable | Purpose |
| --- | --- |
| `CAR_SERVICE_DB_URL` | Apache Derby JDBC URL. |
| `CAR_SERVICE_DB_USER` | Database username. |
| `CAR_SERVICE_DB_PASSWORD` | Database password. |

Use `.env.example` as a safe placeholder template.

## Run Command

Maven prototype:

```bash
cd source/domain-model-maven/carservice-center
mvn exec:java
```

Web applications:

Run from NetBeans or deploy through the included Ant project files to a Java EE server.

## Build Command

Maven prototype:

```bash
cd source/domain-model-maven/carservice-center
mvn compile
```

NetBeans/Ant web app:

```bash
cd source/java-web/WebApplication1
ant
```

## Test Command

The main web app includes TestNG model tests:

```bash
cd source/java-web/WebApplication1
ant test
```

## API Endpoint Summary

Servlet routes include:

| Route | Purpose |
| --- | --- |
| `/signup` | Customer signup. |
| `/login` | Customer login. |
| `/CreateBooking` | Booking page and schedule flow. |

## Screenshots

No public screenshots are committed in this sanitized source publish.

## Known Limitations

- Web-app execution depends on local NetBeans/Java EE server configuration.
- Derby database schema setup is expected locally.
- Large Office/PDF/archive/generated files are intentionally excluded.

## Possible Future Improvements

- Add a portable SQL schema file.
- Replace direct JDBC helper usage with a DAO/service layer.
- Add a Maven or Gradle build for the JSP/Servlet application.

## Security And Configuration Notes

Database URL, user, and password are read from environment variables. Real database credentials, build output, WAR files, local NetBeans private files, archives, and nested Git metadata are excluded.
