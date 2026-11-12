# TYPEX AI — Phase 2 Keyboard Engine

## Implemented in this phase
- Native `InputMethodService` keyboard view.
- English QWERTY key rows.
- Shift and caps-lock toggle.
- Number/symbol page toggle.
- Space, comma, period and enter.
- Editor action handling for Search, Send, Done, Next, Go and Previous.
- Backspace and repeat-delete while held.
- Basic password-field detection to avoid personalized input behavior.
- Language-switch button placeholder for the Bengali engine planned in Phase 3.

## Test checklist
1. Install the debug APK.
2. Open Android Settings → System → Languages & input → On-screen keyboard → Manage on-screen keyboards.
3. Enable TYPEX AI.
4. Open a notes app or browser text field and select TYPEX AI from the input method picker.
5. Verify letters, uppercase, symbols, spaces, punctuation, delete and enter/editor actions.

## Current limitations
- Bengali transliteration and Bengali native layout are not implemented yet.
- Prediction, autocorrect, swipe typing, voice input, clipboard and AI are future phases.
- Emoji globe key currently switches a placeholder mode only.
- Keyboard UI is a functional engineering foundation and will receive visual polish in the theme phase.
- This project has not been device-tested or APK-built in this environment.
