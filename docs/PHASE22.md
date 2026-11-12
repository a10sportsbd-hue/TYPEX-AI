# Phase 22 — Advanced Bengali Typing Pipeline

## Added
- A single offline Bengali typing pipeline for Roman normalization, phrase conversion, candidate analysis and Unicode NFC normalization.
- Expanded common-word vocabulary for everyday Bengali, education, technology, family and conversational typing.
- Alias normalization for a small set of frequent Roman spelling variants.
- Phrase conversion preserves spaces, punctuation, emoji and numeric tokens.
- Candidate ranking now has deterministic exact/prefix/typo ordering and supports contextual hints.

## Scope and limitations
- This is a deterministic lexicon/rule-based engine. It is **not** a complete Avro phonetic implementation, neural language model or context-aware generative model.
- Roman spellings can be ambiguous; the pipeline does not silently rewrite committed text. Existing IME candidate selection remains the user-controlled commit path.
- Existing user vocabulary storage and personal learning behavior are unchanged.
- The new pipeline is exposed as a reusable engine; the existing live IME flow remains compatible and is not replaced wholesale in this phase.

## Tests added
- Phrase conversion with punctuation and whitespace.
- Expanded vocabulary coverage.
- Roman alias normalization.
- Primary/alternative candidate analysis.
- Preservation of numeric and emoji tokens.

## Validation
Source-level checks are performed when packaging. A Gradle build and on-device IME test have not been run in this environment.
