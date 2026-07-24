# Security And Privacy

## Sanitization Applied

- The original Derby database URL/user/password literals were replaced with environment variables in the public `dbconnect.java`.
- Office documents, archives, local IDE files, generated WAR/build output, and private database files are excluded.
- `.env.example` uses placeholders only.

## Security Caveats

- The original prototype stores and validates plain credentials through direct database queries. This should be replaced with hashed passwords before real use.
- Card payment handling is an academic prototype and must not be used with real card data.
- JDBC connection handling should be modernized with a connection pool and centralized DAO/service layer.

## Privacy Caveats

Do not use real customer, vehicle, card, or schedule data in the public repository.
