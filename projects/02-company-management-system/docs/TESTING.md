# Testing And Validation

## Available Validation

No dedicated automated test project was found in the original solution. The primary validation is compile-time:

```bash
dotnet build source/CompanyProject.sln
```

## Manual Review Checklist

- Login page loads as the default route.
- Registration creates an account record.
- Login accepts a stored account.
- Department CRUD pages render and persist.
- Employee creation rejects missing department IDs.
- Employee CRUD pages render and persist.

## Test Gaps

- No unit tests for repositories.
- No integration tests for MVC actions.
- No browser/UI tests for Razor flows.
