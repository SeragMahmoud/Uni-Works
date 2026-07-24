# Uni-Works

Professional university-project portfolio by [SeragMahmoud](https://github.com/SeragMahmoud), organized as five independent GitHub-ready project folders.

The repository combines robotics, full-stack web development, AI product architecture, software-engineering lifecycle work, and software project management. Each project folder includes a detailed README, documentation, diagrams, setup or review guidance, security notes, and selected safe implementation evidence.

## Projects

| # | Project | Type | Stack / Focus | Link |
| --- | --- | --- | --- | --- |
| 1 | Chess ARM | Hardware-dependent source portfolio | Python, OpenCV, Stockfish, PySerial, Arduino/ESP32, servo control | [projects/01-chess-arm](projects/01-chess-arm/) |
| 2 | Company Management System | Runnable web-app source with local DB setup | .NET 8, ASP.NET Core MVC, EF Core, SQL Server, Razor, Bootstrap | [projects/02-company-management-system](projects/02-company-management-system/) |
| 3 | Intelligent Interview Simulation System | Sanitized AI product case study | Flutter, Node/Express, MongoDB, WebSockets, AI backend integration, multimodal feedback | [projects/03-intelligent-interview-simulation-system](projects/03-intelligent-interview-simulation-system/) |
| 4 | Car Service Center | Software-engineering lifecycle and Java prototype | Java, Maven, JSP, Servlets, JDBC, Apache Derby, UML, TestNG artifacts | [projects/04-car-service-center-software-engineering](projects/04-car-service-center-software-engineering/) |
| 5 | IISS Software Project Management | Documentation and governance case study | PMBOK-inspired planning, lifecycle artifacts, communication, RAID, change control, closure | [projects/05-iiss-software-project-management](projects/05-iiss-software-project-management/) |

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

## Review Guidance

- Use each project README as the starting point.
- Use `docs/` inside each project for architecture, setup, testing, security, and limitations.
- Use `.env.example` files only as placeholder templates. Real `.env` files are intentionally excluded.
- Some projects are hardware-dependent or private-service-dependent, so they are presented as source-backed portfolio case studies rather than guaranteed one-command demos.

## Security Notice

The public repository excludes real credentials, `.env` files, database secrets, local absolute paths, private logs, generated dependency/build folders, raw private documents, large model files, and sensitive media.

## License

Project-specific reuse rights are not granted automatically. Each project folder includes `LICENSE-NOT-INCLUDED.md` unless a future explicit license is added.
