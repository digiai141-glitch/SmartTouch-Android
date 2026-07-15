# SmartTouch

<p align="center">
  <img src="docs/banner.png" alt="SmartTouch banner" width="600" />
</p>

<p align="center">
  <a href="https://github.com/your-org/smarttouch/actions/workflows/build.yml">
    <img src="https://github.com/your-org/smarttouch/actions/workflows/build.yml/badge.svg" alt="CI" />
  </a>
  <a href="LICENSE">
    <img src="https://img.shields.io/badge/License-MIT-blue.svg" alt="MIT License" />
  </a>
  <img src="https://img.shields.io/badge/Android-10%2B-brightgreen.svg" alt="Android 10+" />
  <img src="https://img.shields.io/badge/Privacy-100%25%20Offline-orange.svg" alt="100% Offline" />
  <img src="https://img.shields.io/badge/No-Analytics-red.svg" alt="No Analytics" />
</p>

> **SmartTouch** is a privacy-first, universal Android gesture control application. Assign any action to any gesture zone — without ever sending a single byte of data outside your device.

---

## Features

| Category | Details |
|---|---|
| **Gesture Zones** | Top Notch · Top Edge · Left Edge · Right Edge · Bottom Edge · Floating Trigger |
| **Gesture Types** | Single Tap · Double Tap · Long Press · Swipe Left/Right/Up/Down |
| **Actions** | Screen Lock · Flashlight · Volume · Mute · Vibrate · Media Controls · Quick Settings · Notification Panel · Camera · Assistant · Screenshot · Open Any App |
| **Customisation** | Sensitivity · Zone Size · Haptic Feedback · Import/Export · Reset |
| **Privacy** | 100% offline · No internet permission · No ads · No analytics · No Firebase · No telemetry |

---

## Supported Devices

Android 10+ (API 29+) on:
Samsung · Pixel · Realme · OPPO · Vivo · Motorola · Nothing · OnePlus · Xiaomi · Redmi · POCO

---

## Architecture

```
SmartTouch/
├── app/
│   └── src/main/java/com/smarttouch/app/
│       ├── data/
│       │   ├── datastore/          # DataStore (preferences persistence)
│       │   ├── model/              # GestureZone, GestureType, GestureAction, GestureMapping, AppSettings
│       │   └── repository/         # Repository interfaces + implementations
│       ├── di/                     # Hilt modules
│       ├── domain/
│       │   └── usecase/            # Clean Architecture use cases
│       ├── presentation/
│       │   ├── navigation/         # Compose NavGraph
│       │   ├── ui/
│       │   │   ├── components/     # Reusable Compose components
│       │   │   ├── screens/        # Home · GestureConfig · Settings · About
│       │   │   └── theme/          # Material 3 colour, type, theme
│       │   └── viewmodel/          # MVVM ViewModels (Hilt-injected)
│       ├── receiver/               # BootReceiver
│       └── service/                # OverlayService · AccessibilityService · GestureDetector · ActionExecutor
└── gradle/
    └── libs.versions.toml          # Version catalog
```

**Stack:** Kotlin · Jetpack Compose · Material 3 · MVVM · Clean Architecture · Hilt DI · DataStore · Coroutines/Flow

---

## Getting Started

### Prerequisites

- Android Studio Ladybug (2024.2) or later
- JDK 17
- Android SDK 35

### Clone & Build

```bash
git clone https://github.com/your-org/smarttouch.git
cd smarttouch

# Debug APK
./gradlew assembleDebug

# Release APK (requires signing config — see below)
./gradlew assembleRelease

# Run unit tests
./gradlew test

# Run lint
./gradlew lint
```

### Signing a Release Build

Set these environment variables (or GitHub Secrets):

| Variable | Description |
|---|---|
| `KEYSTORE_PATH` | Path to your `.keystore` file |
| `KEYSTORE_PASSWORD` | Keystore password |
| `KEY_ALIAS` | Key alias |
| `KEY_PASSWORD` | Key password |

---

## Permissions Explained

| Permission | Why it's needed |
|---|---|
| `SYSTEM_ALERT_WINDOW` | Display transparent gesture zones on top of all apps |
| `FOREGROUND_SERVICE` | Keep the overlay service alive |
| `VIBRATE` | Haptic feedback when a gesture fires |
| `CAMERA` | Flashlight control via CameraManager (no photos taken) |
| `EXPAND_STATUS_BAR` | Expand notification / quick-settings panel |
| `RECEIVE_BOOT_COMPLETED` | Re-enable service after reboot (if configured) |
| `POST_NOTIFICATIONS` | Show the persistent "service active" notification (Android 13+) |

**No `INTERNET` permission is declared or used.**

---

## Privacy Policy

SmartTouch collects **zero** data. There are no analytics libraries, no crash reporters, no ad SDKs, and no network calls of any kind. All gesture processing happens entirely on your device and is never transmitted anywhere.

See [`SECURITY.md`](SECURITY.md) for the security disclosure process.

---

## Contributing

Pull requests are welcome! Please read [`CONTRIBUTING.md`](CONTRIBUTING.md) first.

---

## Changelog

See [`CHANGELOG.md`](CHANGELOG.md).

---

## License

```
MIT License

Copyright (c) 2025 SmartTouch Contributors

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.
```

See the full [`LICENSE`](LICENSE) file.
