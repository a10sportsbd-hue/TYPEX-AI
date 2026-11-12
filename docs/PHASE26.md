# Phase 26 — Smart Keyboard Intelligence

## Implemented
- Added `SmartKeyboardIntelligence` for local punctuation ranking and sentence-boundary behavior.
- Suggestion bar now offers punctuation chips alongside next-word and phrase suggestions after a committed word.
- Question-like words (for example, English `where` / `why` / `how`, Bengali `কেন` / `কোথায়` / `কি`) prioritize `?`.
- Ordinary words prioritize `.`, then `!`, then `?`.
- English sentence-start capitalization is applied to the first alphabetic key after editor start, `.`, `!`, `?` or newline.
- Bengali typing is not affected by English capitalization logic.
- Sentence completion capitalization helper is available for future completion UI integration.

## Behavior and privacy
- Punctuation chips are user-selected; no punctuation is inserted automatically.
- No surrounding target-app text is read. Sentence state is tracked from keys entered through TYPEX during the active editor session.
- Existing password-field protections remain in place; this phase does not add content capture.

## Tests / validation
- Added five unit tests for question punctuation ranking, ordinary punctuation ranking, capitalization, sentence boundary state and completion capitalization.
- Source-level checks verify service integration and absence of malformed escaped imports.
- Gradle build and physical Android IME testing have not been run in this environment.

## Limitations
- Punctuation ranking is heuristic and based on the last committed word only.
- Capitalization state is local to the current TYPEX input session and may not perfectly infer sentence state when users move the cursor or edit existing text.
- This is deterministic keyboard logic, not a trained AI model.
