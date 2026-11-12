# Phase 14 — Personal Learning & Mixed-Language Prediction

## Added
- Opt-in personal vocabulary learning, disabled by default.
- A dashboard screen to enable/disable learning, review saved words, remove individual words, or clear the vocabulary.
- Words are saved only after an explicit long-press on an English suggestion while learning is enabled.
- Bengali suggestions can be long-pressed to explicitly save the current Roman spelling and selected Bengali word into the custom dictionary.
- English prefix predictions now include the user's explicitly accepted local vocabulary.
- Conservative English typo-correction candidate is surfaced as a suggestion; it is never silently applied.
- Added unit tests for personal vocabulary prediction and typo correction.

## Privacy and limitations
- TYPEX does not inspect surrounding text or automatically harvest typed content to build the vocabulary.
- Learning data is stored locally in app-private SharedPreferences and is not encrypted. Do not save passwords, payment details or other secrets.
- Learning is disabled by default. The user must enable it and explicitly long-press a suggestion to save it.
- This is a lightweight local lexicon, not an adaptive neural language model. Predictions are not guaranteed to be contextually correct.
- Mixed Bengali/English composition still relies on the existing explicit language-mode toggle; seamless script detection is not implemented.
- No Gradle build or physical-device test has been run in this environment.
