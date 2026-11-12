# Phase 19 — Gesture Typing & Swipe Engine Upgrade

## Added
- Restored/declared the swipe gesture state and English swipe engine references in the IME service.
- English swipe candidate matching now compresses repeated adjacent path letters before ranking.
- Added Bengali swipe candidate engine using the local Roman-to-Bengali dictionary, including user custom dictionary entries loaded when the IME service is created.
- Swipe is enabled in Bengali mode as well as English mode. Selecting a swipe candidate commits the word to the focused editor.
- Password fields and symbol mode remain excluded from swipe typing.
- Added subtle key press scale feedback and haptic feedback when a swipe candidate is accepted (if vibration is enabled).
- Added unit tests for Bengali swipe mapping, short path rejection, repeated-letter path handling and unrelated path rejection.

## Important limitations
- Both English and Bengali swipe engines are lexicon/rule-based candidate rankers, not neural models.
- Bengali swipe only recognizes words present in the built-in or custom Roman-to-Bengali dictionary; unknown words fall back to a normal tap.
- Gesture sampling uses key hit-testing against the visible QWERTY keys. There is no predictive curve interpolation, visual swipe trail, or adaptive personal swipe model yet.
- No Gradle build or physical-device test has been run. Validate gesture capture on a real device, including fast diagonal swipes, keyboard edges and touch cancellation.
