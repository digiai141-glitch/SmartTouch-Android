# Security Policy

## Supported Versions

| Version | Supported |
|---------|-----------|
| 1.x     | ✅ Yes     |

## Reporting a Vulnerability

If you discover a security vulnerability in SmartTouch, **please do not open a public issue**.

Instead, please report it privately by:

1. **GitHub Private Reporting** (preferred): Use [GitHub's private vulnerability reporting](https://docs.github.com/en/code-security/security-advisories/guidance-on-reporting-and-writing/privately-reporting-a-security-vulnerability) on this repository.
2. **Email**: Send details to `security@your-org.example` with the subject line `[SECURITY] SmartTouch — <brief description>`.

### What to Include

- A clear description of the vulnerability
- Steps to reproduce
- Impact assessment (who is affected, under what conditions)
- Any proof-of-concept code (if safe to share)
- Your suggested fix (optional but appreciated)

### Response Timeline

- **Acknowledgement**: within 48 hours
- **Initial assessment**: within 7 days
- **Patch / advisory**: within 30 days of confirmation (faster for critical issues)

### Out of Scope

The following are **not** considered vulnerabilities for SmartTouch:

- Issues that require physical access to an already unlocked device
- Abuse of the Accessibility Service by the device owner themselves (it is the user's deliberate choice to enable it)
- Android OS-level issues outside SmartTouch's control

## Privacy Commitment

SmartTouch has **no internet permission** and performs zero data collection, analytics, or telemetry. There is no server infrastructure to compromise. All processing is on-device only.
