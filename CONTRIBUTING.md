# Contributing to SmartTouch

Thank you for your interest in SmartTouch! Contributions of all kinds are welcome.

---

## Code of Conduct

Be respectful and constructive. Harassment or exclusionary behaviour will not be tolerated.

---

## Privacy First — Non-Negotiable Rules

SmartTouch is committed to being 100% privacy-respecting. **All contributions must comply with these rules:**

- ❌ No internet permission (`INTERNET`)
- ❌ No analytics libraries (Firebase, Mixpanel, Amplitude, etc.)
- ❌ No crash reporting services (Crashlytics, Sentry, etc.)
- ❌ No ad SDKs
- ❌ No telemetry of any kind
- ❌ No data transmission off-device

Pull requests that violate these rules will be closed without review.

---

## Getting Started

1. Fork the repository.
2. Clone your fork: `git clone https://github.com/YOUR-USERNAME/smarttouch.git`
3. Open in Android Studio Ladybug 2024.2+.
4. Create a feature branch: `git checkout -b feature/your-feature-name`
5. Make your changes.
6. Run checks before committing:
   ```bash
   ./gradlew lint test
   ```
7. Commit with a clear message (see below).
8. Push and open a Pull Request.

---

## Coding Style

- Follow Kotlin official coding conventions.
- Use `ktlint` formatting (the CI lint step will catch violations).
- Keep functions small and focused.
- Write KDoc for public APIs.
- Prefer `Flow` + `StateFlow` over callbacks.
- No `Log.d/v` calls in release builds (they are stripped by ProGuard, but still avoid them).

---

## Commit Messages

Use [Conventional Commits](https://www.conventionalcommits.org/):

```
feat: add notification panel swipe-down action
fix: correct swipe-up threshold calculation
docs: update README setup instructions
chore: bump AGP to 8.7.3
test: add unit tests for GestureDetector swipe detection
```

---

## Pull Request Checklist

- [ ] Lint passes (`./gradlew lint`)
- [ ] All tests pass (`./gradlew test`)
- [ ] No internet permission / analytics / telemetry added
- [ ] Tested on a physical device (Android 10+)
- [ ] `CHANGELOG.md` updated under `[Unreleased]`
- [ ] PR template filled in

---

## Adding a New Action

1. Add the action to `GestureAction` enum in `data/model/GestureAction.kt`.
2. Add the display string to `res/values/strings.xml`.
3. Implement the action in `ActionExecutor.kt`.
4. Write a unit test covering the new code path.
5. Update `CHANGELOG.md`.

---

## Adding a New Gesture Zone

1. Add the zone to `GestureZone` enum in `data/model/GestureZone.kt`.
2. Add the display string to `res/values/strings.xml`.
3. Add the overlay layout parameters in `OverlayService.rebuildOverlays()`.
4. Update `CHANGELOG.md`.

---

## Questions?

Open a [Discussion](https://github.com/your-org/smarttouch/discussions) or tag your issue with `question`.
