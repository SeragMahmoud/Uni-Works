# Servlet Reference

| Servlet/controller | Route evidence | Purpose |
| --- | --- | --- |
| `SignupController` | `@WebServlet(... urlPatterns = {"/signup"})` | Reads signup form fields, creates `Customer`, calls `dbconnect.signupCustomer`, redirects to login. |
| `LoginController` | Source controller | Validates credentials and starts user session flow. |
| `BookingController` | Source controller | Loads available schedules and forwards booking data. |
| `PaymentController` | Source controller | Handles payment submission. |
| `CashPaymentController` | Source controller | Supports cash payment flow. |
| `ScheduleController` | Source controller | Supports schedule-related behavior. |

The project uses server-rendered JSP pages and servlet requests, not a REST JSON API.
