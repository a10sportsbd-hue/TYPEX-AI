# TYPEX AI Architecture

## Phase 1 boundaries
- Native Android application shell using Kotlin and Jetpack Compose.
- InputMethodService is declared in the manifest and registered as an IME.
- No typed text is collected, logged, or transmitted.
- No AI network calls or model downloads are included in this foundation.

## Planned modules
- keyboard: IME lifecycle, key rendering, input connection, layout engine.
- languages: Bengali phonetic transliteration, layouts, dictionaries and suggestions.
- ai: explicit user-triggered AI actions, provider abstraction and on-device inference.
- privacy: sensitive-field detection, incognito state and data controls.
- storage: Room and DataStore persistence.

## Package
`com.typex.ai`

## Build requirements
- JDK 17
- Android SDK Platform 36
- Gradle 8.13
- Android Gradle Plugin 8.13.2
- Kotlin 2.2.21
