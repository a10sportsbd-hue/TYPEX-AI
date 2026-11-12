# TYPEX AI

**Tagline:** Type beyond limits.

TYPEX AI is a mobile-first Android keyboard project focused on English and Bengali typing, offline-first language suggestions, customizable keyboard themes, optional AI assistance, voice dictation entry points, and opt-in local personal vocabulary learning.

## Development status

- Phase 1–28: source developed progressively in the project workspace.
- Phase 29 onward: planned next development stages.
- APK: not built yet. APK packaging and real-device validation are intentionally deferred until feature development is complete.

## Main capabilities in the current source

- Android `InputMethodService` keyboard foundation.
- English QWERTY and Bengali phonetic typing pipeline.
- Rule-based local suggestions, autocorrect candidates, phrase completion, and swipe candidate ranking.
- Keyboard themes, layout presets, spacing and sizing controls.
- Explicit clipboard history, emoji search, text shortcuts, and system speech-recognition launch.
- Optional online AI provider and deterministic offline helper utilities.
- Opt-in English/Bengali personal learning controls and editable learned-word lists.

## Build

See [`docs/BUILD.md`](docs/BUILD.md). A successful build or device test has not yet been claimed.

## Privacy

See [`docs/PRIVACY.md`](docs/PRIVACY.md). Personal learning is opt-in and stored locally in app-private preferences; this storage is not encrypted. Avoid saving sensitive words or credentials.

## Important limitations

Some language intelligence is dictionary/rule-based rather than a trained neural language model. The offline assistant is a deterministic helper, not a full offline generative model. Voice dictation relies on Android speech-recognition services. Online AI use requires a provider endpoint and API key; shipping a client-side key is not recommended for production.
