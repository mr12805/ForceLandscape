# Force Landscape

A small Android app with a Quick Settings tile.

## Intended behavior

1. Open an app while the device is portrait.
2. Pull down Quick Settings.
3. Tap **Force Landscape**.
4. The device is forced to landscape.
5. Pulling down Quick Settings does not end the session.
6. Leaving the original app with **Back** or **Home** restores the previous rotation settings.

## Required setup on the device

The app needs:

- **Modify system settings** permission.
- **Usage Access** permission so it can identify when the original foreground app has been left.
- The **Force Landscape** tile added to Quick Settings.

## Build

This project uses Android Gradle Plugin 8.6.1, Kotlin 2.0.21, compileSdk 35 and minSdk 26.

It can be built by GitHub Actions without Android Studio on the local PC.

## Important

Android vendors can change rotation behavior. The app currently forces rotation value 1 (90 degrees) for landscape and restores the exact previous `accelerometer_rotation` and `user_rotation` values.

The foreground-app check uses UsageStats and runs while a temporary foreground service is active.
