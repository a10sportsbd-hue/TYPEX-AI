# Phase 9 — Prediction, Emoji Search and Clipboard History

## Added in source
- English next-word prefix suggestions using the local `WordSuggestionEngine`; selecting a candidate replaces the currently tracked token.
- Emoji panel with category filtering for smile, love, gesture and celebrate.
- Clipboard history store with a 15-entry local limit. A user must explicitly open the panel and tap “Save current clipboard to history”; there is no background clipboard polling.
- Recent saved clipboard entries can be inserted by tapping them, or cleared from the panel.
- Existing Bengali suggestion row remains available in Bengali phonetic mode.

## Limitations and privacy
- English prediction is a small bundled word list, not a trained language model; suggestions depend on the currently tracked key input and are not full contextual next-word prediction.
- Emoji search filters category names, not individual emoji descriptions or Unicode names.
- Clipboard history is stored locally in app-private SharedPreferences. It is not encrypted in this phase. Do not save passwords, payment details or other secrets.
- Clipboard access is initiated only by an explicit user tap. Android OS clipboard restrictions may affect availability.
- Swipe typing and a fully customizable layout editor are not implemented.
- Source has not been Gradle-built or tested on a physical device. Compile/runtime verification remains required.
