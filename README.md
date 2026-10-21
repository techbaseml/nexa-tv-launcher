# NEXA TV (prototype)

Standalone ad-free launcher-style app for Android-based Fire TV. Does **not** replace Amazon Home or block ads in other apps.

## Build (no Android Studio)
1. Create an empty GitHub repository.
2. Upload the *contents* of this folder to repository root, preserving `.github/workflows/build.yml`.
3. Open Actions > Build NEXA TV APK > Run workflow (or push to main).
4. Download the `NEXA-TV-debug-APK` workflow artifact, unzip to obtain `app-debug.apk`.
5. From Windows platform-tools: `./adb.exe install -r "C:\path\to\app-debug.apk"` (PowerShell uses `.\adb.exe`).
6. On Fire TV, open NEXA TV from Apps.

## Undo
` .\adb.exe uninstall com.nexa.tv ` removes only this prototype app. No system packages are modified. Keep Amazon Home intact.

## Notes
Requires Fire OS 7 / Android 9 or compatible Android TV; uses QUERY_ALL_PACKAGES for sideloaded installed-app discovery. No network requests, no analytics, no ads. Debug APK is for personal testing, not production distribution. This project has not been tested on your physical device.
