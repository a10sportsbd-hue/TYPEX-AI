# Build TYPEX AI

## Recommended: Android Studio
1. Extract the project ZIP.
2. Open the `TYPEX-AI` folder in Android Studio.
3. Install Android SDK Platform 36 if prompted.
4. Use JDK 17.
5. Sync Gradle.
6. Run the `app` configuration on a device.

## Termux note
A full Android SDK/Gradle build on a phone can be difficult because Android SDK packages and build tools are not always available directly in Termux. Recommended phone-first workflow:
- Edit and manage source with Termux + Git.
- Build using Android Studio on a computer or a cloud CI runner.
- Install the resulting APK on the phone for testing.

## Build command (once Gradle and Android SDK are configured)
`gradle assembleDebug`

APK output:
`app/build/outputs/apk/debug/app-debug.apk`
