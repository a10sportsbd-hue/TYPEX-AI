# Phase 12 — Bengali Prediction & Custom Dictionary

## Added
- Offline Bengali candidate ranker with exact/prefix matching and conservative one-edit typo matching for longer Roman prefixes.
- Small transparent context-hint map for common Bengali phrases; this is a heuristic, not an AI language model.
- User-managed Roman-to-Bengali custom dictionary stored locally in app-private preferences.
- Dashboard entry for adding, listing and deleting custom Bengali words.
- The Bengali IME composition and commit path now checks the custom dictionary before the built-in transliterator.
- Added unit tests for prefix suggestions and contextual hints.

## Limitations
- This remains a starter phonetic engine, not full Avro compatibility. It does not yet provide complete phoneme rules, robust conjunct/reph handling or context-sensitive Bengali orthographic correction.
- Context hints are a small fixed map and not learned from private typing.
- The custom dictionary is local but not encrypted. It should not be used to store secrets.
- The IME suggestions are based on the current Roman composition prefix; previous-word context is not yet reliably extracted from the target app.
- No Gradle build or real-device test has been performed in this environment.
