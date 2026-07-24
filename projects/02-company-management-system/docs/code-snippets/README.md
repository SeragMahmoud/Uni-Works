# Code Snippets

| Public file | Original relative source | Purpose | Sanitization |
| --- | --- | --- | --- |
| `source/CompanyProject/Program.cs` | `CompanyProject/Program.cs` | MVC startup and login-first route. | No secret values present. |
| `source/Company.DAL/context/CompanyDBContext.cs` | `Company.DAL/context/CompanyDBContext.cs` | EF Core context and DbSets. | Local SQL Server connection literal replaced with `COMPANY_DB_CONNECTION_STRING`. |
| `source/CompanyProject/Controllers/AccountController.cs` | `CompanyProject/Controllers/AccountController.cs` | Registration and login flow. | No secret values present. |
| `source/Company.Reposatory/EmployeeRepo.cs` | `Company.Reposatory/EmployeeRepo.cs` | Repository CRUD plus department existence check. | No secret values present. |
| `source/Company.DAL/Entites/*.cs` | `Company.DAL/Entites/*.cs` | Account, department, and employee data models. | No secret values present. |
