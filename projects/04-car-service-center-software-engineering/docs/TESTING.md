# Testing And Validation

## Test Artifacts Found

The original web app includes TestNG files for:

- `User`
- `Service`
- `Schedule`
- `CashPayment`
- `CardPayment`
- `Booking`

## Important Accuracy Note

The TestNG files are generated prototype tests. They contain `fail("The test case is a prototype.")` and often instantiate `null` objects. They should be read as evidence that the project considered model-level verification, not as a passing automated test suite.

## Recommended Validation Commands

Maven domain model:

```bash
cd source/domain-model-maven/carservice-center
mvn compile
```

NetBeans web app:

```bash
cd source/java-web/WebApplication1
ant
```

`ant test` is expected to fail until generated prototype tests are completed.
