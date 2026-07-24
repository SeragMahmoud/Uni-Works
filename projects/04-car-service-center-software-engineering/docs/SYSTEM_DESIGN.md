# System Design

## System Actors

- Customer: signs up, logs in, browses services, books, pays, and reviews.
- Administrator: manages services, schedules, bookings, technicians, and reports conceptually.
- Technician: receives assignments and views schedules conceptually.

## System Modules

```mermaid
flowchart TD
    Account[Signup and login] --> CustomerFlow[Customer service flow]
    CustomerFlow --> Booking[Booking]
    Booking --> Schedule[Schedule]
    Booking --> Payment[Payment]
    Payment --> Card[Card payment]
    Payment --> Cash[Cash payment]
    Admin[Admin management] --> Service[Service catalogue]
    Admin --> Technician[Technician assignment]
    CustomerFlow --> Review[Review]
```
