# System Design

## Design Style

The solution follows a classic MVC plus repository pattern:

- Razor Views render forms and tables.
- Controllers validate input and coordinate actions.
- Repositories perform data operations.
- `CompanyDBContext` maps entities to SQL Server.

## Main Workflows

```mermaid
flowchart TD
    Login[Login] --> Home[Home page]
    Home --> Dept[Department management]
    Home --> Emp[Employee management]
    Dept --> DeptCrud[Create, details, update, delete]
    Emp --> EmpCrud[Create, update, delete]
    EmpCrud --> DeptCheck[Department existence check]
```
