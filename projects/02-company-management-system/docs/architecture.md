# Architecture

The application uses a layered ASP.NET Core MVC architecture:

```text
Razor Views -> MVC Controllers -> Repository Layer -> EF Core DbContext -> SQL Server
```

## Layers

| Layer | Location | Responsibility |
| --- | --- | --- |
| Web UI | `source/CompanyProject/Views` | Razor pages and Bootstrap-based user interface. |
| Controllers | `source/CompanyProject/Controllers` | Request handling and workflow coordination. |
| Repository | `source/Company.Reposatory` | Data operations for accounts, departments, and employees. |
| Data access | `source/Company.DAL` | Entities, EF Core DbContext, configuration, and migrations. |

## Routing

The default MVC route is:

```text
{controller=Account}/{action=Login}/{id?}
```

This creates a focused login-first entry point.

