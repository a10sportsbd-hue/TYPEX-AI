# TYPEX AI — Phase 3 Bengali Typing Engine

## Implemented
- Offline Roman-to-Bengali transliteration engine.
- Common Bengali word dictionary overrides.
- Longest-match consonant and vowel mapping.
- Bengali mode switch from the keyboard bottom row.
- Composing text while the user types Roman letters.
- Candidate strip using local suggestions.
- Backspace edits the active Roman composition buffer.
- Space, punctuation and Enter commit the current Bengali word.
- Unit tests for common transliteration examples.

## Quick test
1. Enable TYPEX AI in Android keyboard settings.
2. Open a normal text field and select TYPEX AI.
3. Tap `EN` to switch to Bengali mode.
4. Type `ami`, then press space: expected `আমি `.
5. Try `tumi`, `bangla`, `bhalo`, `dhonnobad`.
6. Tap `EN` again to return to English.

## Limitations
- This is an initial offline phonetic engine, not a complete Avro-compatible implementation.
- Transliteration quality is dictionary-led for common words and rule-based for unknown words.
- Full conjunct, reph, kar-phala, punctuation normalization, expanded dictionaries and personalized prediction require further work.
- Arabic, Hindi and Urdu are not implemented yet.
- Device build and runtime testing remain necessary.
