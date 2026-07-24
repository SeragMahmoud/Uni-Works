# Code Snippets

The public folder does not mirror private source. These snippets are short, sanitized adaptations derived from the Original implementation and documented for architecture review.

## Express Startup And Route Registration

Original relative source: `Code/Z_project_final (1)/.../backend_webfront/main.js`

Sanitization: local URLs and environment values are omitted; route names are preserved.

```js
import 'dotenv/config';
import express from 'express';
import http from 'http';
import { setupInterviewWS } from './src/websocket/InterviewSocket.js';

const app = express();
app.use(express.json({ limit: '10mb' }));

app.use('/api/auth', authRoutes);
app.use('/api/interviews', interviewRoutes);
app.use('/api/progress-dashboard', progressDashboardRoutes);
app.use('/api/company', companyRoutes);
app.use('/api/billing', billingRoutes);
app.use('/api/admin', adminRoutes);

const server = http.createServer(app);
setupInterviewWS(server);
server.listen(process.env.PORT || 3000);
```

## WebSocket Interview Bridge

Original relative source: `Code/Z_project_final (1)/.../backend_temp/src/websocket/InterviewSocket.js`

Sanitization: private service URLs are replaced by environment-based placeholders.

```js
const sendAiEnvelope = (ws, event, payload = {}) => {
  ws.send(JSON.stringify({
    event,
    timestamp: new Date().toISOString(),
    request_id: crypto.randomUUID(),
    payload,
  }));
};

clientSocket.on('message', (message, isBinary) => {
  if (isBinary) {
    sendAiEnvelope(aiSocket, 'video_frame', {
      frame_b64: Buffer.from(message).toString('base64'),
    });
    return;
  }

  const parsed = JSON.parse(message.toString());
  if (parsed.event === 'audio_chunk') {
    sendAiEnvelope(aiSocket, 'audio_chunk', parsed.payload);
  }
});
```

## Flutter Interview Service

Original relative source: `Code/Z_project_final (1)/.../gp_implementation/lib/services/api/interview_service.dart`

Sanitization: class shape and multipart behavior are shown without private endpoint values.

```dart
class InterviewService {
  InterviewService({ApiClient? apiClient})
      : _apiClient = apiClient ?? ApiClient();

  final ApiClient _apiClient;

  Future<HrInterviewConfigurationResult> createHrInterviewConfiguration({
    required String token,
    required HrInterviewConfigurationRequest request,
  }) async {
    final files = <ApiMultipartFile>[];
    if (request.cvPdfBytes != null) {
      files.add(ApiMultipartFile(
        fieldName: 'cvPdf',
        fileName: request.cvPdfFileName!,
        bytes: request.cvPdfBytes!,
      ));
    }
    return _apiClient.postMultipart(
      HrInterviewConfigurationRoutes.path,
      token: token,
      fields: request.toFields(),
      files: files,
    );
  }
}
```

## AI Client Boundary

Original relative source: `Code/Z_project_final (1)/.../backend_temp/src/integrations/ai/AiApiClient.js`

Sanitization: private AI base URL removed.

```js
const apiBase = `${trimSlash(process.env.AI_API_BASE_URL)}${process.env.AI_API_PREFIX || '/api/v1'}`;

export const initInterview = async (payload) =>
  postJson('/interviews/init', payload);

export const endInterviewSession = async (sessionId) =>
  postJson(`/interviews/${sessionId}/end`);

export const getInterviewFeedback = async (sessionId) =>
  getJson(`/interviews/${sessionId}/feedback`);
```
