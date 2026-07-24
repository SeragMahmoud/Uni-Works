# Route Reference

This project is a Razor MVC web application, so routes serve pages and form submissions instead of a JSON API.

| Route | Method | Purpose |
| --- | --- | --- |
| `/Account/Login` | GET | Render login page. |
| `/Account/Login` | POST | Validate login credentials. |
| `/Account/Create` | GET | Render registration page. |
| `/Account/Create` | POST | Create a new account. |
| `/Department/Index` | GET | List departments. |
| `/Department/Create` | GET/POST | Create a department. |
| `/Department/Details/{id}` | GET | View department details. |
| `/Department/Update/{id}` | GET/POST | Update department details. |
| `/Department/Delete/{id}` | GET/POST | Delete a department. |
| `/Employee/Index` | GET | List employees. |
| `/Employee/Create` | GET/POST | Create an employee. |
| `/Employee/update/{id}` | GET/POST | Update an employee. |
| `/Employee/Delete/{id}` | GET/POST | Delete an employee. |
