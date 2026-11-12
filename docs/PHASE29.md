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
## Layout modes added in this increment
- Added selectable Avro (Roman QWERTY), Probhat (direct Unicode) and National/Jatiya (direct Unicode) modes.
- Added a persistent layout preference. While Bengali mode is active, the toolbar layout control cycles Avro → Probhat → Jatiya.
- Direct layouts use Unicode output and do not pass Bengali characters through the Roman transliteration path.
- Added tests for key maps and case-sensitive Avro-style retroflex keys.
- Backspace now attempts code-point deletion through InputConnection before falling back to one UTF-16 code unit.

## Validation boundaries
- The key maps are an initial mobile QWERTY adaptation and still need comparison against the official desktop/mobile layouts and real-user validation. They are not yet certified as exact standards-compliant implementations.
- Avro transliteration has improved case-sensitive retroflex support, but is not yet a complete Avro-compatible engine.
- National/Probhat shift layers, conjunct formation, kar/reph automation, candidate handling, and cursor/text-editing gestures remain to be completed.


CI validation is being iterated on the current branch before merging to main.

## Text navigation and editing increment
- Added horizontal drag on the spacebar to move the editor cursor by Unicode code point using the current InputConnection extracted text.
- Added an explicit text-edit panel with Select all, Cut, Copy and Paste through Android's native context-menu actions.
- Editing actions are suppressed in password/sensitive fields.
- These controls require validation across different target apps because some editors restrict context-menu actions or extracted-text access.
