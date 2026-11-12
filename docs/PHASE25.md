# Phase 25 — Smart Bengali Suggestion Bar Integration

## Implemented
- The live Bengali Roman composition suggestion bar now passes the previous committed Bengali word into the Bengali candidate ranker.
- Current-token candidates are combined from the Bengali lexicon engine and the Phase 22 Bengali typing pipeline.
- Custom Bengali dictionary entries remain part of the candidate dictionary.
- Mixed English/Bengali resolution remains in the candidate list for Bengali-mode typing.
- Conservative Bengali typo correction remains a separate selectable chip; it does not silently replace text.
- Expanded Bengali context hints for common conversational sequences such as `আমি → ভালো/আছি`, `তুমি → কেমন/আছো`, and `ধন্যবাদ → আপনাকে/অনেক`.
- Existing committed-word next-word suggestions and phrase completions remain available.

## Privacy and interaction
- Only TYPEX's tracked Roman composition and last committed word are used.
- The keyboard does not read arbitrary text around the cursor in the target application.
- The user must tap a candidate to accept it. Long-press custom dictionary saving remains available.
- Existing password-field exclusions are retained.

## Tests / validation
- Added three unit tests for context-aware Bengali candidates, common conversational hints and custom dictionary inclusion.
- Source-level checks verify pipeline integration, previous-word context, custom dictionary merge and correction selection.
- Gradle build and Android device testing have not been run in this environment.

## Limitations
- Bengali context prediction uses a small hand-authored local ruleset, not a trained language model.
- Roman-to-Bengali conversion remains lexicon/rule based and is not a complete Avro implementation.
