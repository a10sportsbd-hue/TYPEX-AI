# Phase 17 — Smart Bengali, Emoji Intelligence & Toolbar Upgrade

## Added
- Conservative Roman-to-Bengali autocorrect candidate engine using a one-edit lexicon match. It proposes corrections only; it never silently changes typed text.
- Bengali correction suggestion is integrated into the live Roman composition suggestion row.
- Offline emoji search catalogue with English and Bengali keywords, including smile, love, gestures, celebrations, nature, food and animals.
- Emoji panel now searches semantic keywords rather than only filtering four fixed category names.
- Added a dedicated AI Studio toolbar shortcut (✨). Tapping it launches TYPEX AI's AI Studio screen through an explicit activity intent.
- The voice toolbar shortcut now routes to the Voice Dictation screen through the same explicit launch-extra mechanism.
- Added unit tests for Bengali autocorrect and bilingual emoji search.
- Dashboard content remains scrollable; no redesign of existing feature screens was removed.

## Limitations
- Bengali autocorrect is a small lexicon-based one-edit helper, not a complete Bengali spellchecker. It may miss valid variants or suggest a wrong near-match.
- Emoji catalogue is intentionally compact and offline; it is not a full Unicode emoji database or natural-language embedding search.
- AI toolbar opens AI Studio; it does not automatically read or send text from the focused app.
- No Gradle build or physical Android device test has been run in this environment.
