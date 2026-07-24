# Setup And Usage

## Prerequisites

- .NET 8 SDK.
- SQL Server or SQL Server Developer/Express.
- EF Core CLI tools if applying migrations from the command line.

## Build

```bash
dotnet restore source/CompanyProject.sln
dotnet build source/CompanyProject.sln
```

## Configure Database

Set the connection string in your local environment:

```bash
COMPANY_DB_CONNECTION_STRING=Server=.;Database=CompanyprojectDB;Trusted_Connection=True;TrustServerCertificate=True;
```

## Apply Migrations

```bash
dotnet ef database update --project source/Company.DAL --startup-project source/CompanyProject
```

## Run

```bash
dotnet run --project source/CompanyProject/CompanyProject.csproj
```

Open the printed local URL and start at `/Account/Login`.
