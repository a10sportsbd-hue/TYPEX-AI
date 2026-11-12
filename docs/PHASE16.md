# Phase 16 — Autocorrect, Phrase Completion, Dictionary & Typing Stats

## Added
- Conservative local English autocorrect candidate engine. Corrections are displayed as an explicit `Fix:` suggestion and are never silently applied.
- Phrase completion suggestions for a small set of English and Bengali context words.
- English ↔ Bengali searchable starter dictionary in the app dashboard.
- Optional local typing statistics dashboard with aggregate letter/space key presses, word boundaries and accepted suggestions.
- Statistics are disabled by default and store counters only, never typed text or message content.
- Password fields are excluded from suggestion rows and stats counting.
- Added unit tests for autocorrect, phrase completion and bilingual dictionary search.
- Repaired a literal escaped newline in keyboard service imports found during source inspection.

## Limitations
- Autocorrect and phrase completion use small local word/phrase lists; they are not a neural language model and can miss errors.
- The bilingual dictionary is a starter vocabulary, not a comprehensive dictionary or translation service.
- Typing stats are aggregate counters only; they do not represent accurate words-per-minute or language-specific analytics.
- Stats are stored in app-private SharedPreferences and are not encrypted. Counters are buffered in memory and periodically flushed to reduce per-keystroke storage writes.
- No Gradle build or physical Android device test has been run in this environment.
