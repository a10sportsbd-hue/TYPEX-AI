# Phase 21 — Keyboard Layout Presets & Key Appearance

## Added
- Three selectable keyboard layout presets: QWERTY, AZERTY and QWERTZ.
- Extracted layout row definitions into `KeyboardLayoutEngine` so the preset data can be unit-tested independently.
- Theme Studio now exposes layout selection, key corner radius (4–18 dp), key text size (16–24 sp), and horizontal keyboard side inset (0–14 dp).
- The IME reads the selected layout and visual preferences when rendering its keyboard.
- Existing theme, key height, spacing, compact mode, toolbar, swipe preview, sound, vibration and animation settings remain available.
- Added unit tests for supported layouts, safe fallback, alphabet coverage and QWERTZ Y/Z placement.

## How to configure
Open TYPEX AI → Theme Studio → Advanced Keyboard Controls. Choose QWERTY, AZERTY or QWERTZ and adjust the key shape, text size and side inset. Reopen the keyboard to apply the updated layout.

## Scope and limitations
- Layout customization currently means selecting one of three tested presets. It is not yet a drag-and-drop editor and does not support arbitrary key remapping.
- Key shape control changes corner radius; it does not yet offer separate rounded, square and pill profiles.
- No Gradle build or physical Android device test has been run. Validate all three layouts, Bengali phonetic input and narrow-screen rendering on-device.
