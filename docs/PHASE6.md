# TYPEX AI — Phase 6 AI Integration Foundation

## Implemented
- AI provider abstraction (`AiProvider`).
- Online OpenAI-compatible Chat Completions client.
- User-entered API base URL, model ID and API key.
- API key is held only in the AI Studio screen's in-memory state; it is not written to preferences or source files.
- Explicit AI Studio screen with user-entered text and action selection.
- Actions: grammar, rewrite, professional tone, shorten, expand, Bangla-to-English, English-to-Bangla and smart reply.
- Offline deterministic fallback utilities and small phrase dictionary.
- Clear offline labeling: offline tools are not represented as an on-device LLM.
- Network permission added only for online provider calls.
- AI processing is user-triggered; no automatic capture of text typed into other apps.

## Online provider configuration
- Base URL example: `https://api.openai.com/v1`
- Model ID: enter a model enabled by the chosen provider.
- API key: enter at runtime; it is not saved.
- The client calls `<base-url>/chat/completions`.

## Security warning
A client-side API key can still be exposed on a compromised/rooted device or through runtime inspection. For a public production APK, route requests through a trusted backend that protects provider secrets, enforces rate limits and validates requests. Do not publish a shared private key inside the app.

## Offline limitations
The offline provider is a small deterministic phrase dictionary and basic text utility, not a local generative AI model. A genuine offline LLM requires a compatible model file, license review, device RAM/storage assessment, and an inference runtime integration.

## Not yet implemented
- Actual on-device LLM/runtime and downloadable model manager.
- Streaming responses, token limits and cancellation.
- Secure backend proxy, account quotas and abuse prevention.
- AI keyboard toolbar integration for explicitly selected text.
- Thorough multilingual translation coverage.
- Android device build/runtime tests.
