# Setup And Usage

## Review Mode

This public folder is intended for portfolio review, not direct execution.

Recommended review path:

1. Read `README.md` for project scope.
2. Review `docs/TECHNICAL_ARCHITECTURE.md` for runtime architecture.
3. Open `assets/screenshots/` for public UI evidence.
4. Read `docs/code-snippets/README.md` for sanitized implementation excerpts.
5. Read `docs/SECURITY_AND_PRIVACY.md` for publication boundaries.

## Why The Full App Is Not Published Here

The Original implementation includes:

- `.env` files with private runtime values.
- Local network IPs and machine paths.
- Logs and private test reports.
- Large generated builds, model files, archives, and datasets.
- Identifiable webcam images in some screenshots.

Publishing a complete runnable mirror would risk exposing sensitive implementation and personal data.

## Minimal Runtime Requirements In The Original

The private implementation evidence indicates these runtime areas:

- Node.js backend with Express and MongoDB.
- Flutter web/mobile/desktop clients.
- AI backend reachable through HTTP and WebSocket URLs.
- Local or remote speech, CV, question-generation, and feedback services.
- Email and billing-related configuration.
