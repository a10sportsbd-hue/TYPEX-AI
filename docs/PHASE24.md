# Phase 24 — Smart Suggestion Bar Integration

## Implemented
- Connected `AdvancedEnglishTypingEngine` to the live English typed-token suggestion bar.
- English candidate bar now combines the existing personal dictionary predictor with Phase 23 contextual/prefix candidates.
- Contraction expansion is offered as a selectable candidate (for example, `im` → `I'm`).
- Conservative English spelling corrections are offered as candidates, not applied automatically.
- When a token has been committed and the keyboard is waiting for the next word, English next-word hints from the advanced engine are merged with the existing phrase and sentence hints.
- Candidate selection continues to replace only TYPEX's tracked current token; no target-app surrounding text is harvested.
- Password fields remain excluded from English suggestions and correction behavior through the existing service guard.

## Interaction behavior
- The user taps a candidate to accept it.
- Long-press behavior continues to add accepted English candidates to the local personal vocabulary.
- Accepted candidate frequency is updated using the existing local vocabulary store.
- No silent autocorrect or automatic contraction expansion.

## Tests / validation
- Reused the Phase 23 engine's six unit tests.
- Source-level checks verify the service import, engine instance, typed-token candidate merge, next-word merge and password guard.
- Gradle build and real Android IME tests have not been run in this environment.

## Limitations
- The service currently tracks only its own typed token and last committed word. It does not read arbitrary surrounding text from the target application to infer full sentence context.
- Contextual next-word prediction is limited to the engine's local starter rules and dictionary.
