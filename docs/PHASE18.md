# Phase 18 — Advanced Keyboard Settings & Layout Controls

## Added
- Key spacing preference (0–6 dp), saved locally and applied to letter and bottom-key rows.
- Compact keyboard toggle; reduces active key height while retaining the configured base height.
- Toolbar visibility toggle, allowing users to reclaim keyboard space.
- Configurable one-hand keyboard width (250–350 dp), used when the keyboard's one-hand toolbar control is active.
- Advanced controls added to Theme Studio; the screen is vertically scrollable and the theme preset grid has a bounded height.
- Existing theme selection, key height, sound and vibration settings are retained.
- Added unit tests for theme preset uniqueness, preset count and corner-radius bounds.

## How to use
Open TYPEX AI → Theme Studio → Advanced Keyboard Controls. Change spacing, compact mode, toolbar visibility or one-hand width. Reopen the keyboard to apply the saved layout settings. The one-hand width setting takes effect when the ↔ toolbar control is enabled.

## Limitations / verification
- Settings use app-private SharedPreferences; they are not encrypted, but contain only UI preferences.
- The one-hand width is a width preference; it does not add drag-to-position behavior.
- No Gradle build or physical-device test has been run in this environment. Run the included unit tests and verify layout on a real Android device, especially at narrow screen widths and with the toolbar disabled.
