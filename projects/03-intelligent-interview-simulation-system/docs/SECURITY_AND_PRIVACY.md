# Security And Privacy

## Excluded Sensitive Material

- Original `.env` files.
- API keys, JWT secrets, database URIs, email credentials, payment configuration, and local IPs.
- Logs and private test reports.
- Large datasets and model binaries.
- Screenshots containing identifiable webcam imagery.
- Raw demo/promo videos and copyrighted research PDFs.

## Privacy Considerations

The full IISS product handles candidate CVs, speech, video frames, facial signals, gaze/tone metrics, identity images, and interview reports. In a real deployment these should be protected with:

- Clear consent and retention policies.
- Encryption in transit and at rest.
- Access control by role.
- Audit logs for recruiter/admin access.
- Secure deletion for media and CV uploads.
- Careful disclosure around AI-generated feedback limitations.

## Implementation Notes From Source

- Backend code redacts sensitive request fields in logs.
- JWT-style authentication is used in the Original dependency set.
- Role and subscription middleware exist in the implementation tree.
- Identity verification can block interview WebSocket startup.

## Public Repository Boundary

This case study is intentionally non-runnable. The boundary avoids publishing secrets, private local configuration, personal media, or large artifacts that do not belong in a portfolio repository.
