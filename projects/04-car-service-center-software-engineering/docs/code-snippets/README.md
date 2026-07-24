# Code Snippets

| Public file | Original relative source | Purpose | Sanitization |
| --- | --- | --- | --- |
| `source/domain-model-maven/.../Booking.java` | `Implementation/carservice-center/.../Booking.java` | Booking lifecycle, service association, technician assignment, cancellation/update/confirmation. | No credential values present. |
| `source/java-web/WebApplication1/src/java/Controller/SignupController.java` | `full_project_of_SE2/WebApplication1/src/java/Controller/SignupController.java` | Servlet signup flow. | No secret values present. |
| `source/java-web/WebApplication1/src/java/model/dbconnect.java` | `full_project_of_SE2/WebApplication1/src/java/model/dbconnect.java` | JDBC access for login, booking, schedules, and payments. | Original Derby username/password replaced with `CAR_SERVICE_DB_*` environment variables. |
| `source/java-web/WebApplication1/test/model/*.java` | `full_project_of_SE2/WebApplication1/test/model/*.java` | Generated TestNG test skeletons. | Published as testing evidence; documented as incomplete/prototype. |
