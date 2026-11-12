# Phase 20 — Swipe Path Preview, Gesture Controls & UI Polish

## Added
- Live swipe path preview above the key rows. It displays the sampled key sequence and labels Bengali-mode gestures separately.
- Swipe path preview can be enabled/disabled from Theme Studio → Advanced Keyboard Controls.
- Key press scale animations can now be enabled/disabled independently.
- Gesture path is capped at 32 sampled keys to prevent unbounded path growth.
- Bengali swipe normalizes repeated adjacent samples before candidate scoring, matching the English swipe engine behavior.
- Preview state is cleared after gesture completion or touch cancellation.
- Existing password-field and symbol-mode swipe exclusions remain active.
- Added a Bengali swipe normalization test.

## Performance and behavior notes
- Swipe candidate scoring runs once on gesture release, not on every move event.
- Touch moves only append a key when the pointer enters a different key; repeated samples of the same key are ignored.
- The preview is a text-based path indicator, not a drawn continuous finger-trail overlay.
- The project still uses dictionary-backed swipe candidate ranking rather than a neural model.

## Verification status
- Source-level wiring, Kotlin delimiter checks and archive integrity checks are performed for this phase.
- No Gradle build or physical Android device test has been run. Verify preview visibility, touch cancellation, gesture accuracy and animation settings on-device.
