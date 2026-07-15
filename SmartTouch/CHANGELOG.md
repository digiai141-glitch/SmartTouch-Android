# Changelog

All notable changes to SmartTouch are documented here.

The format follows [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

---

## [Unreleased]

### Planned
- Per-zone enable/disable toggle in the UI
- Import / Export settings as JSON file
- Floating trigger overlay with drag-to-position
- Per-mapping long-press duration customisation
- Night/Day auto theme preference

---

## [1.0.0] — 2025-07-15

### Added
- **Six gesture zones**: Top Notch, Top Edge, Left Edge, Right Edge, Bottom Edge, Floating Trigger
- **Seven gesture types per zone**: Single Tap, Double Tap, Long Press, Swipe Left/Right/Up/Down
- **Sixteen actions**: Screen Lock, Flashlight, Volume Up/Down, Mute, Vibrate, Media Play-Pause/Next/Prev, Quick Settings, Notification Panel, Camera, Assistant, Screenshot, Open Any App
- Custom `GestureDetector` with configurable sensitivity (1–10 scale)
- `OverlayService` foreground service — transparent overlay windows covering each gesture zone
- `SmartTouchAccessibilityService` — minimal service used only for global actions (no content inspection)
- `BootReceiver` — optional auto-start after device reboot
- **DataStore-backed persistence**: gesture-to-action mappings + app settings survive app reinstall
- **Material 3 dynamic colour** UI with full dark/light mode support
- Haptic feedback on gesture recognition (configurable)
- Gesture zone size configuration (8–64 dp)
- Reset-all-settings confirmation dialog
- Full privacy notice in About screen
- Zero internet permission — 100% offline
- Unit tests for `GestureDetector`, `GestureMapping`, and `AppSettings`
- GitHub Actions CI: lint, unit tests, debug APK, release APK
- MIT license
- `README`, `CONTRIBUTING`, `SECURITY`, `CHANGELOG` open-source files
- GitHub Issue and PR templates

---

[Unreleased]: https://github.com/your-org/smarttouch/compare/v1.0.0...HEAD
[1.0.0]: https://github.com/your-org/smarttouch/releases/tag/v1.0.0
