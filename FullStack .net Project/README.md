# Company Management System - ASP.NET Core MVC

This repository contains a full-stack company management web application built with ASP.NET Core MVC, Entity Framework Core, SQL Server, Razor Views, Bootstrap, and a layered C# solution structure.

The project provides account registration/login, department management, employee management, relational data modeling, repository-based persistence, and a clean MVC user workflow.

## Repository Structure

| Folder | Purpose |
| --- | --- |
| [source/](source/README.md) | Visual Studio solution and .NET projects. |
| [docs/](docs/README.md) | Architecture, setup, database, and workflow documentation. |
| [diagrams/](diagrams/README.md) | Architecture, database, and MVC flow diagrams. |
| [demo/](demo/README.md) | Screenshots, screen recordings, and demo media. |
| [assets/](assets/README.md) | Images and visual assets used in documentation. |
| [reports/](reports/README.md) | Technical reports and review-ready summaries. |
| [templates/](templates/README.md) | Reusable documentation and planning templates. |
| [archive/](archive/README.md) | Preserved supporting material and workspace metadata. |

## Technology Stack

| Area | Technologies |
| --- | --- |
| Framework | ASP.NET Core MVC |
| Runtime | .NET 8 |
| Language | C# |
| ORM | Entity Framework Core 8 |
| Database | SQL Server |
| UI | Razor Views, Bootstrap |
| Client-side support | jQuery, jQuery Validation |
| IDE | Visual Studio |

## Solution Projects

| Project | Role |
| --- | --- |
| `CompanyProject` | MVC web application with controllers, views, layout, and static assets. |
| `Company.DAL` | Data access layer with entities, DbContext, configuration, and migrations. |
| `Company.Reposatory` | Repository layer for account, department, and employee persistence operations. |

## Main Features

- User account registration.
- Login-first application flow.
- Department create, read, update, delete workflow.
- Employee create, read, update, delete workflow.
- Employee-to-department relationship.
- SQL Server persistence through Entity Framework Core.
- Razor forms with validation attributes.
- Bootstrap-based responsive web interface.

## Quick Start

Build the solution:

```bash
dotnet build source/CompanyProject.sln
```

Run the MVC web application:

```bash
dotnet run --project source/CompanyProject/CompanyProject.csproj
```

Apply EF Core migrations from the web project context when preparing the database:

```bash
dotnet ef database update --project source/Company.DAL --startup-project source/CompanyProject
```

## Default Application Flow

The application opens at:

```text
Account/Login
```

After login, users can access the home page and manage departments and employees through MVC controller routes and Razor Views.

## Portfolio Value

This project demonstrates a strong enterprise-style ASP.NET Core MVC foundation with layered architecture, SQL Server persistence, repository abstraction, entity relationships, and complete CRUD workflows.
