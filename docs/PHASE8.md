# Phase 8 — Voice, Shortcuts and Compact Layout

## Added to the source
- Voice Typing screen uses Android's system speech-recognition intent.
- Microphone permission is requested only after the user taps Start voice typing.
- Recognized text is displayed for review and can be copied to the clipboard. It is not injected into another app automatically.
- Local Text Shortcut manager supports adding, listing and deleting trigger/expansion pairs.
- Keyboard checks locally tracked word tokens and expands matching shortcuts at a word boundary. Password fields are excluded.
- One-hand layout toggle narrows the keyboard view to a 300 dp width.

## Important limitations
- Speech recognition depends on the device's installed speech service and language packs; it may require internet. This is not offline speech recognition.
- Shortcut matching is a first implementation: trigger tokens are case-normalized and whitespace/punctuation boundaries are used. Multi-word triggers, cursor-aware replacement and advanced Bengali transliteration integration need more work.
- One-hand mode is a basic right-aligned compact layout, not a full draggable floating keyboard.
- Swipe typing is not implemented in this phase.
- No automatic microphone activation, continuous audio capture, background clipboard monitoring, or automatic transmission of typed text.
- This source has not been Gradle-built or device-tested. Treat these as implementation candidates until verified on a real Android device.
