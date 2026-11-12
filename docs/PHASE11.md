# Phase 11 — Live Swipe Gesture Capture

## Added in source
- Letter keys now collect pointer movement using Android touch events.
- The active touch path is mapped against visible QWERTY letter-key bounds.
- Repeated hits on the same key are collapsed into a key path.
- On finger-up, the local `SwipeTypingEngine` ranks vocabulary candidates; the top qualifying candidate is committed into the focused text field.
- Swipe recognition is disabled in Bengali phonetic mode, symbols mode and password fields. Normal taps still type individual letters.
- Candidate ranker now applies a minimum score threshold to reduce unrelated suggestions.
- Added a negative-path unit test.

## Important limitations
- This is a small lexicon-based first swipe implementation, not a production neural gesture model. It will have limited vocabulary and accuracy.
- No visual trail overlay, adaptive language model, personalized gesture learning or multi-language swipe dictionary yet.
- Bengali swipe input is not implemented; Bengali phonetic typing remains separate.
- The gesture listener has not been compiled or tested on physical devices. Touch dispatch behavior should be validated across Android versions and different keyboard sizes.
