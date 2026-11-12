# TYPEX AI — Phase 4 Language & Prediction Foundation

## Implemented
- Expanded offline Bengali word lexicon and phonetic mappings.
- Longest-match transliteration rules for common Roman letter combinations.
- Bengali digit conversion for 0–9.
- Local candidate suggestions from the Bengali dictionary.
- Reusable offline English prefix prediction engine.
- Conservative edit-distance correction helper.
- Local custom dictionary persistence using Android SharedPreferences.
- Language pack registry for English, Bengali, Arabic, Hindi and Urdu.
- Availability is explicit: English and Bengali are enabled; Arabic, Hindi and Urdu are metadata placeholders only.
- Unit tests for English prefix prediction and typo correction.

## Important implementation boundary
The current IME candidate strip displays Bengali candidates while Bengali mode is active. The English prediction/correction engine is implemented as a reusable component but is not yet connected to text-before-cursor extraction or automatic replacement. This avoids reading surrounding text from apps without a deliberate, privacy-reviewed interaction.

## Test
Run local unit tests:
`gradle testDebugUnitTest`

Manual Bengali checks:
- `ami` → `আমি`
- `bangla` → `বাংলা`
- `dhonnobad` → `ধন্যবাদ`
- `shundor` → `সুন্দর`

## Remaining work
- Avro-grade contextual transliteration, conjunct handling and reph rules.
- Integrate opt-in English prediction and correction in the IME.
- Add user interface for custom dictionary management.
- Implement full Arabic/Hindi/Urdu key layouts and shaping support.
- Run Android device tests and performance profiling.
