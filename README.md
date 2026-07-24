# Uni-Works

Uni-Works is a professional university-project monorepo by [SeragMahmoud](https://github.com/SeragMahmoud). It collects five academic software, AI, robotics, and project-management projects in one clean GitHub portfolio.

Each project is organized independently under `projects/` with its own documentation, setup notes, source code or safe project artifacts, and configuration guidance.

## Projects

| # | Project | Description | Technology Stack | Link |
| --- | --- | --- | --- | --- |
| 1 | Chess ARM | Robotic chess player that connects camera-based board detection, Stockfish chess decisions, and servo-arm movement. | Python, OpenCV, NumPy, python-chess, Stockfish, PySerial, Tkinter, Arduino/ESP32 C++ | [projects/01-chess-arm](projects/01-chess-arm/) |
| 2 | Company Management System | ASP.NET Core MVC web application for account, department, and employee management. | .NET 8, C#, ASP.NET Core MVC, Entity Framework Core, SQL Server, Razor, Bootstrap | [projects/02-company-management-system](projects/02-company-management-system/) |
| 3 | Intelligent Interview Simulation System | AI interview-simulation concept with avatar interaction, speech workflows, multimodal analysis, research, and system design documentation. | AI system design, DeepSeek workflow concept, speech-to-text, text-to-speech, facial analysis, tone analysis, verification design | [projects/03-intelligent-interview-simulation-system](projects/03-intelligent-interview-simulation-system/) |
| 4 | Car Service Center | Software Engineering 1 and 2 case study with requirements, UML/design artifacts, Java domain model, JSP/Servlet web apps, Derby database access, and tests. | Java, Maven, Java EE, JSP, Servlets, JDBC, Apache Derby, NetBeans Ant, TestNG, UML | [projects/04-car-service-center-software-engineering](projects/04-car-service-center-software-engineering/) |
| 5 | IISS Software Project Management | Project-management case study for IISS covering initiation, planning, monitoring/control, closure, scheduling, communication, RAID, and governance artifacts. | Microsoft Project workflow, PMBOK-inspired documentation, Word/PowerPoint planning artifacts summarized in Markdown | [projects/05-iiss-software-project-management](projects/05-iiss-software-project-management/) |

## Repository Structure

```text
Uni-Works/
  README.md
  .gitignore
  .gitattributes
  projects/
    01-chess-arm/
    02-company-management-system/
    03-intelligent-interview-simulation-system/
    04-car-service-center-software-engineering/
    05-iiss-software-project-management/
```

## Setup Expectations

Each project has its own setup instructions inside its folder. In general:

- Python projects use `requirements.txt`.
- .NET projects use `dotnet build` and `dotnet run`.
- Java projects use Maven or NetBeans/Ant project files.
- Documentation-focused projects can be reviewed directly through Markdown files.

## Security Notice

Secrets and private local configuration are intentionally excluded. Real `.env` files, credentials, database connection strings, private keys, local databases, generated build output, dependency folders, and large opaque binaries are not committed.

Where a project needs configuration, use the included `.env.example` file as a safe template and provide real values only in your local environment.

## Author

Created and maintained by [SeragMahmoud](https://github.com/SeragMahmoud).

Individual setup instructions are available inside each project folder.
