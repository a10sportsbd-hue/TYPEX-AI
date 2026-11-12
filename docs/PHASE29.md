# Phase 29 — Keyboard Core Recovery

## Goal
Stabilize the Bengali/English keyboard input path, restore core editor controls, and make the critical input engines independently testable.

## Implemented
- Extracted Bengali Roman composition into `BengaliCompositionController` with preview, commit, backspace and clear operations.
- Added selectable Avro-phonetic, Probhat and National/Jatiya Bengali layouts, persisted in preferences and cycled from the keyboard toolbar.
- Added case-sensitive Avro-style keys for common retroflex letters (`T`, `Th`, `D`, `Dh`, `Sh`). This is still a partial phonetic rule set, not a complete Avro implementation.
- Transcribed normal and Shift letter mappings for Probhat and National/Jatiya from the upstream OpenBangla layout JSON definitions.
- Fixed touch dispatch so Avro preserves the displayed case-sensitive key and direct layouts resolve the logical QWERTY key before emitting Unicode.
- Added horizontal spacebar cursor navigation using the current editor connection.
- Added a native edit panel with Select all, Cut, Copy and Paste actions; sensitive/password fields are excluded.
- Added `UnicodeDeletionEngine` for Bengali combining marks and virama-linked conjunct deletion.
- Added unit tests for layout maps, touch dispatch, phonetic key case, and Unicode deletion.
- Added third-party attribution and the upstream GPL-3.0 license text in `THIRD_PARTY_NOTICES.md` and `licenses/OPENBANGLA-GPL-3.0.txt`.

## CI verification
- GitHub Actions run [#28](https://github.com/a10sportsbd-hue/TYPEX-AI/actions/runs/43199459363) completed successfully on 2026-11-12.
- `gradle testDebugUnitTest assembleDebug` passed and produced the `TYPEX-AI-debug-apk` artifact.
- This confirms the code compiles, the unit-test task passes, and the debug APK packages on the GitHub runner. It does not replace real-device testing.

## Remaining work before Phase 29 can be called complete
- Test composition, selection, cursor movement and delete behavior on common editors (messaging, browser, notes and password fields).
- Validate all three Bengali layouts with native Bengali typists and compare key-by-key on Android devices.
- Complete Avro phonetic rules and candidate/orthography handling; current rules are only a partial compatibility layer.
- Improve direct-layout handling for vowel signs, conjunct composition, reph/ফলা and long-press alternate characters.
- Verify accessibility, touch targets, key repeat, editor action behavior and orientation/resizing.
- Run full regression tests after those changes and publish a release APK only after device validation.

## Source attribution
The Probhat and National/Jatiya key tables were transcribed from:
- https://github.com/OpenBangla/OpenBangla-Keyboard/blob/master/data/Probhat.json
- https://github.com/OpenBangla/OpenBangla-Keyboard/blob/master/data/National_Jatiya.json

These upstream layout definitions are distributed with the OpenBangla project under GPL-3.0; see the included license notice.
