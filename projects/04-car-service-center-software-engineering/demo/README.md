# Demo Review Guide

## Local Demo

1. Configure `CAR_SERVICE_DB_URL`, `CAR_SERVICE_DB_USER`, and `CAR_SERVICE_DB_PASSWORD`.
2. Build the Maven domain model with `mvn compile`.
3. Open `source/java-web/WebApplication1` in NetBeans or run `ant` in that folder.
4. Deploy to a compatible Java EE server.
5. Review signup, login, service browsing, booking, and payment pages.

## Documentation Demo

If the Java EE environment is unavailable:

- Review `docs/PROJECT_OVERVIEW.md` for lifecycle mapping.
- Review `diagrams/` for preserved UML/design evidence.
- Review `source/domain-model-maven` for domain classes.
- Review `source/java-web/WebApplication1/src/java/Controller` for servlet flows.
