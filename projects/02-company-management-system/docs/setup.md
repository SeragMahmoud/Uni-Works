# Setup

## Build

```bash
dotnet build source/CompanyProject.sln
```

## Run

```bash
dotnet run --project source/CompanyProject/CompanyProject.csproj
```

## Database

Apply migrations:

```bash
dotnet ef database update --project source/Company.DAL --startup-project source/CompanyProject
```

The SQL Server database name is:

```text
CompanyprojectDB
```

