# Security And Privacy

## Sanitization Applied

- The public DbContext reads `COMPANY_DB_CONNECTION_STRING` from the environment.
- No real `.env` file is included.
- Generated build output and user-specific Visual Studio metadata are ignored and removed from the GitHub-ready tree.

## Current Security Model

The original project uses a simple academic account table with email and password fields. This is acceptable as a learning CRUD prototype but should not be treated as production authentication.

## Recommended Hardening

- Use ASP.NET Core Identity with password hashing and account lockout.
- Inject `DbContext` and repositories through dependency injection.
- Move all secrets and connection strings into user secrets, environment variables, or a managed secret store.
- Add validation and authorization checks around department and employee actions.
