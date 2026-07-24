# API And Module Reference

This reference summarizes route groups observed in the Original Express implementation. It is not a complete runnable API contract.

| Route group | Purpose |
| --- | --- |
| `/api/auth` | Candidate, HR, and admin authentication/profile flow. |
| `/api/interviews` | Interview creation, HR configuration, token launch, reports, and verification. |
| `/api/progress-dashboard` | Candidate progress and report data. |
| `/api/company` | Company profile and update flows. |
| `/api/hrmanager` | HR manager profile and configuration flows. |
| `/api/billing` | Billing workflow. |
| `/api/payments` | Payment workflow. |
| `/api/subscription` | Subscription creation/details/cancel flow. |
| `/api/subscription-plan` | Subscription-plan listing and management. |
| `/api/admin` | Admin overview and management flow. |
| `/api/support` | Candidate and HR support flow. |
| `/ws/interview` | Realtime bridge for interview media, questions, transcripts, metrics, and completion. |

## WebSocket Event Categories

| Direction | Events |
| --- | --- |
| Client to backend | `video_frame`, `audio_chunk`, `text_answer`, `next_question`, `session_end`, `complete_ack` |
| Backend to client | `session_ready`, `question`, `metric_update`, `transcript_update`, `interview_complete`, `error` |
| Backend to AI | `session_start`, `video_frame`, `audio_chunk`, `next_question`, `session_end` |
