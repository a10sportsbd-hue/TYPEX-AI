# Phase 15 — Mixed Typing, Sentence Prediction & Performance Foundation

## Added
- Mixed-language token resolver for Bengali mode: recognized Bengali phonetic words convert to Bengali, common English words remain English, and user-approved English vocabulary can remain English.
- Custom Bengali dictionary entries take priority over English preservation.
- Lightweight next-word phrase suggestions for a limited set of English and Bengali contexts. Suggestions are explicit and never auto-inserted.
- Personal vocabulary ranking now uses explicit accepted-suggestion counts when learning is enabled.
- English autocorrect remains suggestion-only; no silent replacement.
- Added unit tests for mixed-language resolution, custom dictionary priority and next-word prediction.
- Vocabulary clear now preserves the learning-enabled preference.

## Privacy and limitations
- This is a local heuristic engine, not a neural sentence model. Phrase suggestions only cover a small fixed context map.
- Mixed script recognition uses a curated list of common English words plus user-approved vocabulary; unknown words can still be transliterated unexpectedly.
- Personal learning remains disabled by default and only records accepted suggestions when enabled. It does not read surrounding text.
- No runtime profiling or Android device testing has been performed. The phase is a foundation for performance measurement, not a verified speed benchmark.
- No Gradle build has been run in this environment; compile and test on an Android/Gradle setup before release.
