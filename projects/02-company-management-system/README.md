# Company Management System - ASP.NET Core MVC

## Overview

Company Management System is a full-stack ASP.NET Core MVC application for account registration/login, department management, and employee management. It uses Entity Framework Core and SQL Server through a layered solution structure.

## Purpose

The project demonstrates an enterprise-style MVC CRUD application with separated web, data-access, and repository layers.

## Main Features

- Account registration.
- Login-first application flow.
- Department create, read, update, delete workflow.
- Employee create, read, update, delete workflow.
- Employee-to-department relationship.
- Razor Views and Bootstrap UI.
- EF Core migrations.

## Technology Stack

.NET 8, C#, ASP.NET Core MVC, Entity Framework Core 8, SQL Server, Razor Views, Bootstrap, jQuery, and jQuery Validation.

## Architecture

```text
Razor Views -> MVC Controllers -> Repository Layer -> EF Core DbContext -> SQL Server
```

| Folder | Purpose |
| --- | --- |
| `source/CompanyProject` | ASP.NET Core MVC web application. |
| `source/Company.DAL` | Entities, DbContext, configuration, and migrations. |
| `source/Company.Reposatory` | Repository classes for persistence operations. |
| `docs` | Architecture, database, setup, and workflow documentation. |

## Prerequisites

- .NET 8 SDK.
- SQL Server or compatible SQL Server development instance.
- EF Core CLI tools for database migration commands.

## Installation

Restore and build:

```bash
dotnet build source/CompanyProject.sln
```

## Environment Variables

Set this value locally before running:

| Variable | Purpose |
| --- | --- |
| `COMPANY_DB_CONNECTION_STRING` | SQL Server connection string for the EF Core database. |

Use `.env.example` as a safe placeholder template. Do not commit real connection strings.

## Run Command

```bash
dotnet run --project source/CompanyProject/CompanyProject.csproj
```

## Build Command

```bash
dotnet build source/CompanyProject.sln
```

## Test Command

No automated test project is included.

## API Endpoint Summary

This is an MVC web app. Main routes include:

| Route | Purpose |
| --- | --- |
| `/Account/Login` | Login form and login submission. |
| `/Account/Create` | Registration form and account creation. |
| `/Department` | Department listing and CRUD workflow. |
| `/Employee` | Employee listing and CRUD workflow. |

## Screenshots

No public screenshots are committed in this sanitized source publish.

## Known Limitations

- Runtime requires a configured SQL Server database connection supplied through the environment.
- No dedicated automated test project is included.

## Possible Future Improvements

- Add dependency injection for repositories.
- Add ASP.NET Core Identity.
- Add automated integration tests.

## Security And Configuration Notes

The database connection string is not committed. Generated `bin/obj` output and Visual Studio workspace metadata are excluded.
