# Phase 29 — Keyboard Core Recovery

## Goal
Begin the TYPEX AI rebuild by making Bengali phonetic composition state explicit and independently testable before adding more UI or AI features.

## Implemented in this increment
- Added `BengaliCompositionController` to own the active Roman composition buffer.
- Separated preview, commit, backspace, raw-buffer inspection and clear operations.
- Integrated the controller into the live `InputMethodService` Bengali typing path.
- Added unit tests for live preview (`a` → `আ`, `am` → `আম`, `ami` → `আমি`), commit/reset, backspace and custom dictionary precedence.

## Important status
- This is the first Phase 29 increment, not the complete keyboard upgrade.
- Avro-compatible transliteration is still incomplete; the current phonetic rules and dictionary are not yet equivalent to the full Avro engine.
- National and Probhat layouts are not implemented in this increment.
- No Gradle build or real-device IME test has been run for this branch yet.

## Next Phase 29 work
- Verify composing-region and cursor behavior across editor apps.
- Implement and validate selectable Avro, National and Probhat input methods using documented mappings.
- Recover standard keyboard controls and test Bengali/English switching, backspace, punctuation and editor actions.
- Run automated tests and build the APK only after the Phase 29 scope is complete.