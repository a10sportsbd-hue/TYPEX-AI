# TYPEX AI — Phase 28: Personal Dictionary & Learning Controls

## Delivered
- Independent English and Bengali personal-learning switches.
- Backward compatibility: existing installs inherit the former shared `learning_enabled` value until language-specific settings are first written.
- English learned-word list with per-word acceptance count and remove action.
- Bengali learned-word count list with per-word remove action.
- Separate reset actions for English and Bengali data, each protected by a confirmation dialog.
- Existing global clear API remains available.
- Learning remains opt-in and local to app-private SharedPreferences.

## Privacy and limitations
- No surrounding sentence, clipboard, password, or arbitrary editor text is harvested for learning.
- Words are recorded only on explicit suggestion acceptance while that language's learning switch is enabled.
- Stored SharedPreferences data is not encrypted. Users should avoid treating the personal dictionary as a secure vault.
- This is frequency-based local adaptation, not neural model training.
- No Gradle build or physical Android device test was run in this phase.
