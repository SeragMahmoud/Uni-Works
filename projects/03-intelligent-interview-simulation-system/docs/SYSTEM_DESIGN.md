# System Design

## Design Boundaries

IISS is split into client, API, database, realtime bridge, and AI-service layers.

```mermaid
flowchart TD
    Client[Flutter clients] --> Rest[REST API]
    Client --> Realtime[Realtime WebSocket]
    Rest --> Database[(MongoDB)]
    Realtime --> Ai[AI backend]
    Ai --> Realtime
    Realtime --> Client
    Rest --> Reports[Reports and dashboards]
```

## Session Design

- REST creates or loads interview configuration.
- WebSocket owns realtime media/event exchange.
- AI backend owns question generation, speech/visual signal handling, and feedback.
- Backend persists status, questions, metrics, and final reports.
