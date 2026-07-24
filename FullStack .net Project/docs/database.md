# Database

The application uses Entity Framework Core with SQL Server.

## DbContext

`CompanyDBContext` exposes:

```csharp
DbSet<Department> Departments
DbSet<Employee> Employees
DbSet<Account> Accounts
```

## Entities

| Entity | Purpose |
| --- | --- |
| `Account` | Stores registered user credentials. |
| `Department` | Stores department details such as name, description, code, and creation date. |
| `Employee` | Stores employee details and the related department ID. |

## Relationship

```text
Department 1 -> many Employees
Employee many -> 1 Department
```

The relationship is configured through `EmployeeConfg.cs`.

