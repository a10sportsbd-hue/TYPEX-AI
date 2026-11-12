# Phase 27 — Personalized Learning & Adaptive Prediction

## Implemented
- Added a reusable `AdaptivePredictionRanker` that ranks existing suggestions using explicit local acceptance counts while preserving original relevance order for ties.
- English live suggestion candidates now use the current learned-frequency scores.
- Bengali live suggestion candidates now also use accepted-word frequency scores.
- Added Bengali accepted-word counts to the existing local vocabulary store.
- English and Bengali acceptance learning share the existing explicit `learning_enabled` opt-in.
- Frequency counters are capped at 10,000 per token and English vocabulary storage is capped at 1,000 entries.
- Clear-learning removes both English and Bengali frequency counters.

## Privacy
- Learning remains disabled by default (existing setting).
- Counts are stored locally in Android SharedPreferences.
- No key-by-key stream, sentence, clipboard, target-app content, network upload or cloud profile is created.
- Counts update only when the user taps a suggestion and learning is enabled.
- Long-press dictionary addition remains a separate explicit action.
- SharedPreferences are app-private but not encrypted; this is not suitable for sensitive words.

## Tests / validation
- Added four unit tests for frequency ranking, stable tie order, case-insensitive duplicate removal and limits.
- Source-level checks verify English/Bengali ranking integration, opt-in checks and bounded counters.
- Gradle build and Android device tests have not been run in this environment.

## Limitations
- Personalization changes candidate ordering only; it does not train a neural model.
- Current learned ranking is based on single-word acceptance counts, not full phrase history or semantic understanding.
- Bengali ranking uses the accepted Bengali candidate string as the local score key.
