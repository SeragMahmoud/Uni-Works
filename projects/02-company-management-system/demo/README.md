# Demo Review Guide

## Local Demo

1. Set `COMPANY_DB_CONNECTION_STRING`.
2. Restore and build the solution.
3. Apply EF Core migrations when a local SQL Server instance is available.
4. Run `dotnet run --project source/CompanyProject/CompanyProject.csproj`.
5. Open `/Account/Login`.
6. Register an account, log in, then review department and employee CRUD pages.

## No-Database Review

If SQL Server is unavailable, inspect:

- `source/CompanyProject/Controllers`
- `source/CompanyProject/Views`
- `source/Company.DAL/Entites`
- `source/Company.Reposatory`
- `docs/DATABASE_DESIGN.md`
