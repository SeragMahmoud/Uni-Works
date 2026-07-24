# Technical Architecture

## Web MVC Flow

```mermaid
flowchart TD
    Browser[Browser] --> JSP[JSP view layer]
    JSP --> Signup[SignupController]
    JSP --> Login[LoginController]
    JSP --> Booking[BookingController]
    JSP --> Payment[PaymentController]
    Signup --> Db[dbconnect JDBC helper]
    Login --> Db
    Booking --> Db
    Payment --> Db
    Db --> Derby[(Apache Derby)]
    Db --> Models[Customer Booking Service Schedule Payment]
```

## Domain Model

```mermaid
classDiagram
    class User
    class Customer
    class Administrator
    class Technician
    class Booking
    class Service
    class Schedule
    class Payment
    class CardPayment
    class CashPayment
    class Review

    User <|-- Customer
    User <|-- Administrator
    User <|-- Technician
    Booking --> Service
    Booking --> Payment
    Payment <|-- CardPayment
    Payment <|-- CashPayment
    Customer --> Booking
    Customer --> Review
    Technician --> Schedule
```

## Major Source Areas

| Area | Path | Responsibility |
| --- | --- | --- |
| Domain model | `source/domain-model-maven/carservice-center` | Java 21 object model prototype. |
| Main web app | `source/java-web/WebApplication1` | JSP/Servlet implementation with controllers, models, views, and tests. |
| Additional web app | `source/java-web-carservice/carservice` | Alternate web implementation with admin and booking pages. |
| Design artifacts | `diagrams/*` | Use-case, architecture, class, and sequence artifacts. |

## Data Access Design

`dbconnect.java` uses prepared statements for customer signup/login, bookings, schedules, and payments. The public copy reads database connection settings from environment variables.
