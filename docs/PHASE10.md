# Phase 10 — Swipe Candidate Engine & Bengali Phonetic Expansion

## Added
- `SwipeTypingEngine`: offline candidate ranker that accepts a sequence of nearest QWERTY keys and ranks matching local vocabulary using path coverage and subsequence scoring.
- Expanded Bengali phonetic word lexicon with common conversational words, verb forms and Bangladesh place names.
- Added unit tests for swipe candidate ranking and selected Bengali dictionary entries.
- Existing Bengali live composition and candidate display continue to be used by the IME.

## What is not yet complete
- The swipe ranker is an engine foundation only. The IME does not yet capture continuous finger movement across keys or commit swipe candidates. A proper gesture collector and on-keyboard path overlay are still required.
- Bengali transliteration is still a rules-and-dictionary starter, not a complete Avro-compatible engine. Contextual reordering, robust conjunct handling, reph/hasanta edge cases and a much larger curated lexicon remain future work.
- No model training or neural swipe model is included.
- Gradle build and physical-device tests have not been run in this environment.
