# Phase 7 — Smart Keyboard Toolbar

## Implemented in source
- Explicit-action keyboard toolbar: Emoji, Clipboard, AI Studio launcher and language toggle.
- Local emoji panel with common emoji categories.
- Clipboard insertion only happens after the user taps the Clipboard key; there is no background clipboard polling.
- AI shortcut opens TYPEX AI app without automatically reading the current field or sending typed text.

## Privacy and platform notes
- Clipboard contents can include sensitive data. TYPEX reads only the current primary clip after an explicit tap.
- Android may restrict clipboard access depending on OS version and app focus. If the system returns no clip, nothing is inserted.
- Voice typing is not included in this phase. A production voice feature needs explicit microphone permission, clear recording state and a user-controlled speech-recognition flow.
- Swipe typing and configurable text shortcuts are not claimed as implemented.
- Source has not been built with Gradle or tested on a physical Android device in this environment.
