# Technical Architecture

## System Context

```mermaid
flowchart TD
    Candidate[Candidate] --> Client[Flutter client]
    Recruiter[Recruiter] --> Client
    Admin[Admin] --> Client
    Client --> Rest[Express REST API]
    Client --> InterviewWS[Interview WebSocket]
    Rest --> Mongo[(MongoDB)]
    Rest --> Uploads[CV, question-set, selfie uploads]
    Rest --> Billing[Billing and subscriptions]
    InterviewWS --> AiApi[AI backend HTTP API]
    InterviewWS --> AiSocket[AI backend WebSocket]
    AiApi --> AiServices[Question, CV, feedback, identity services]
    AiSocket --> AiServices
    AiServices --> InterviewWS
    InterviewWS --> Client
```

## Backend Modules

| Module area | Responsibility |
| --- | --- |
| Auth | Candidate, HR, and admin signup/login/profile flows. |
| Interview | Candidate interview sessions, HR configurations, links, reports, and launch flow. |
| WebSocket | Realtime bridge between Flutter clients and AI backend sessions. |
| CV | CV text extraction and AI-normalized role/experience metadata. |
| Billing/payment | Subscription plans, transactions, and payment method flow. |
| Admin | Admin overview and management routes. |
| Support | Candidate/HR support routes. |

## Realtime Interview Lifecycle

```mermaid
stateDiagram-v2
    [*] --> Configured
    Configured --> Joined: candidate opens token
    Joined --> Initialized: backend creates AI session
    Initialized --> Started: WebSocket session_start
    Started --> Streaming: audio/video/text events
    Streaming --> FeedbackPending: session_end
    FeedbackPending --> Completed: feedback received and stored
    FeedbackPending --> Failed: AI timeout or backend error
    Completed --> [*]
    Failed --> [*]
```

## Data Flow

```mermaid
flowchart LR
    CV[CV text or PDF] --> Parse[CV extraction]
    Parse --> Config[Interview configuration]
    Config --> Init[AI interview init payload]
    Init --> Questions[Questions and follow-ups]
    CandidateSignals[Audio, video, answers] --> Metrics[Realtime metrics]
    Questions --> Session[Interview session]
    Metrics --> Session
    Session --> Feedback[Final feedback]
    Feedback --> Report[Candidate and recruiter reports]
```

## Important Engineering Decisions

- The backend loads `.env` before module imports so top-level `process.env` reads see configured values.
- Realtime messages are normalized into explicit event envelopes.
- Candidate-facing completion uses cached final payloads so reconnecting clients can receive final state.
- AI feedback is polled after session end, with a fallback endpoint path in the original WebSocket code.
- Identity verification can block WebSocket session startup when verification is pending or failed.
