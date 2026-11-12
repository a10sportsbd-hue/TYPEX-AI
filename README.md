# TYPEX AI

Premium Android keyboard foundation — Bengali-first, multilingual, online + offline AI-ready.

## Current status: Phase 1 Foundation
- [x] Kotlin Android app shell
- [x] Jetpack Compose dashboard
- [x] InputMethodService manifest registration
- [x] English and Bengali IME subtype declarations
- [x] Privacy baseline documentation
- [x] Functional English QWERTY key layout (Phase 2)
- [x] Initial offline Bengali phonetic transliteration (Phase 3)
- [ ] Online/offline AI (later phases)

## Build
See [docs/BUILD.md](docs/BUILD.md).

## Important
This is the Phase 1 foundation, not a finished keyboard. The IME service currently displays a foundation placeholder. Actual key input, Bengali conversion and AI are deliberately scheduled for later phases.


## Phase 2
See [docs/PHASE2.md](docs/PHASE2.md) for implementation details and device test checklist.

## Phase 3
See [docs/PHASE3.md](docs/PHASE3.md) for Bengali typing implementation and test steps.

## Phase 4
See [docs/PHASE4.md](docs/PHASE4.md) for language packs, prediction and custom dictionary foundation.

## Phase 5
See [docs/PHASE5.md](docs/PHASE5.md) for Theme Studio, presets and keyboard customization.

## Phase 6
See [docs/PHASE6.md](docs/PHASE6.md) for online provider integration, offline text utilities and security boundaries.


## Phase 7
See [docs/PHASE7.md](docs/PHASE7.md) for the smart toolbar, emoji panel and explicit clipboard insertion.


## Phase 8\nSee [docs/PHASE8.md](docs/PHASE8.md) for tap-to-start voice dictation, local text shortcuts and compact one-hand layout.\n

## Phase 9
See [docs/PHASE9.md](docs/PHASE9.md) for English prefix suggestions, emoji category filtering and explicit-save clipboard history.


## Phase 10
See [docs/PHASE10.md](docs/PHASE10.md) for the offline swipe candidate-ranking foundation and expanded Bengali phonetic lexicon.


## Phase 11
See [docs/PHASE11.md](docs/PHASE11.md) for first live QWERTY swipe gesture capture and local candidate commit.


## Phase 12
See [docs/PHASE12.md](docs/PHASE12.md) for Bengali prefix/typo suggestions and the custom Roman-to-Bengali dictionary manager.


## Phase 13
See [docs/PHASE13.md](docs/PHASE13.md) for Bengali conjunct-token expansion and Unicode orthography helpers.


## Phase 14
See [docs/PHASE14.md](docs/PHASE14.md) for opt-in personal vocabulary learning and conservative local typo suggestions.


## Phase 15
See [docs/PHASE15.md](docs/PHASE15.md) for mixed Bengali-English token resolution, local next-word hints and accepted-word ranking.


## Phase 16
See [docs/PHASE16.md](docs/PHASE16.md) for conservative autocorrect, phrase completion, bilingual dictionary and optional aggregate typing statistics.
\n\n## Phase 17\nSee [docs/PHASE17.md](docs/PHASE17.md) for Bengali autocorrect, bilingual emoji search and AI toolbar launch integration.\n

## Phase 18
See [docs/PHASE18.md](docs/PHASE18.md) for advanced keyboard layout preferences and Theme Studio controls.


## Phase 19
See [docs/PHASE19.md](docs/PHASE19.md) for English/Bengali gesture typing and swipe engine improvements.


## Phase 20
See [docs/PHASE20.md](docs/PHASE20.md) for swipe path preview, gesture normalization and keyboard animation controls.


## Phase 21
See [docs/PHASE21.md](docs/PHASE21.md) for QWERTY/AZERTY/QWERTZ presets and key appearance controls.


## Latest: Phase 22 — Advanced Bengali Typing Pipeline

See [`docs/PHASE22.md`](docs/PHASE22.md) for the new offline phrase transliteration pipeline, expanded vocabulary, Roman alias normalization and candidate ranking improvements. This remains a deterministic lexicon/rule-based engine, not a complete Avro clone or neural model.


## Phase 23 — Advanced English Typing & Prediction

See [`docs/PHASE23.md`](docs/PHASE23.md) for the offline English typing engine, contextual next-word suggestions, conservative correction, contractions and sentence-case helper.


## Phase 24 — Smart Suggestion Bar Integration

See [`docs/PHASE24.md`](docs/PHASE24.md). The live English suggestion bar now combines the Phase 23 offline engine with existing personal vocabulary, selectable contraction/correction candidates and contextual next-word hints.


## Phase 25 — Smart Bengali Suggestion Bar Integration

See [`docs/PHASE25.md`](docs/PHASE25.md) for live Bengali candidate integration, contextual ranking, mixed-language support and selectable correction behavior.


## Phase 26 — Smart Keyboard Intelligence

See [`docs/PHASE26.md`](docs/PHASE26.md) for punctuation ranking, English sentence-start capitalization and smart suggestion-bar punctuation chips.


## Phase 27 — Personalized Learning & Adaptive Prediction

See [`docs/PHASE27.md`](docs/PHASE27.md) for opt-in local English/Bengali candidate frequency ranking and bounded local learning counters.


## Phase 28 — Personal Dictionary & Learning Controls
- Independent English/Bengali opt-in learning controls, editable learned-word lists, per-language reset, and legacy preference migration.
- See `docs/PHASE28.md` for privacy and validation notes.

## CI build
GitHub Actions builds a debug APK on pushes to `main` and manual workflow dispatch. The APK is published as the `TYPEX-AI-debug-apk` workflow artifact after a successful build.
