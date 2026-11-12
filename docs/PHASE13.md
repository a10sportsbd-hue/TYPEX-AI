# Phase 13 — Bengali Orthography & Conjunct Foundation

## Added
- Expanded the Roman token table with longer consonant-cluster tokens for common Bengali conjuncts, including ক্ষ, জ্ঞ, ঙ্ক, ঙ্গ, ঞ্জ, ন্দ, ন্ত, ম্প, ম্ব, ক্র, গ্র, ত্র, দ্র, প্র, ব্র, শ্র and স্ত্র.
- Longest-match selection is now applied to vowel tokens too, reducing short-token collisions.
- Added `BengaliOrthography` helpers for supported Roman conjunct lookup and Unicode NFC normalization.
- Added unit tests covering conjunct mappings, Unicode normalization and preservation of core dictionary words.
- The existing IME continues to use the same offline transliteration and custom dictionary pipeline.

## Important limitations
- This is a conjunct foundation, not a complete Avro-compatible engine. Roman-to-Bengali transliteration remains heuristic and some cluster spellings are ambiguous.
- Reph, র-ফলা, য-ফলা and কার placement are represented by Unicode sequences where the engine can produce them; comprehensive context-aware spelling rules are not yet implemented.
- No adaptive learning from typed private text is performed. No surrounding text is harvested.
- No Gradle build or physical-device test has been run here. Validate IME touch behavior and Bengali rendering on the target Android versions before release.
