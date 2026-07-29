# Native Notification Prototype

A minimal Android app demonstrating Firebase Cloud Messaging (FCM) integration with native notification handling.

## Features

- **Get FCM Token**: Button in the app to retrieve and display the device's FCM registration token
- **Foreground Notifications**: When app is open, incoming messages display as an in-app dialog
- **Background Notifications**: When app is closed, incoming messages appear as system notifications with sound, vibration, and lights
- **Logcat Logging**: All events logged to Logcat for debugging

## Architecture

### Components

**MainActivity.kt**
- Jetpack Compose UI with a "Show FCM Token" button
- Displays in-app dialog for foreground notifications
- Lifecycle tracking: calls `setAppInForeground(true/false)` on resume/pause

**MyFirebaseMessagingService.kt**
- Extends `FirebaseMessagingService` (handles incoming FCM messages)
- Publishes messages to a `StateFlow<FCMMessage>` for UI observation
- Conditionally shows system notifications based on app foreground state
- Configures notification channel with HIGH priority, vibration, lights, and sound

## Notification Flow

```
Firebase Cloud Messaging
         |
         v
MyFirebaseMessagingService.onMessageReceived()
         |
         +---> Is app in foreground?
         |
         +---> YES: Skip system notification
         |     Only publish to StateFlow
         |     UI shows AlertDialog
         |
         +---> NO: Send system notification
               (with sound, vibration, lights)
               Also publish to StateFlow
```

## How to Use

### 1. Get the FCM Token

1. Build and run the app on a device or emulator
2. Tap the **"Show FCM Token"** button
3. Token appears as a Toast and is logged to Logcat with tag `FCM`
4. Copy the token for use in sending test messages

### 2. Send a Test Message

**Option A: Firebase Console (Easiest)**
- Go to [Firebase Console](https://console.firebase.google.com/)
- Select your project → **Cloud Messaging**
- Click **Send your first message**
- Compose notification with title and body
- Select target: **Registration token**
- Paste the token from step 1
- Click **Send**

**Option B: REST API (HTTP v1)**
```bash
ACCESS_TOKEN=$(gcloud auth application-default print-access-token)
curl -X POST \
  "https://fcm.googleapis.com/v1/projects/PROJECT_ID/messages:send" \
  -H "Authorization: Bearer $ACCESS_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "message": {
      "token": "DEVICE_TOKEN",
      "notification": {
        "title": "Test Message",
        "body": "Hello from FCM!"
      }
    }
  }'
```

Replace `PROJECT_ID` with your Firebase project ID and `DEVICE_TOKEN` with the token from step 1.

### 3. Observe Behavior

**When app is open:**
- AlertDialog pops up with the message title and body
- No system notification appears
- Message logged: `d/MyFcmService: Notification sent: ...`

**When app is closed:**
- System notification appears in the notification tray
- Sound plays (if device volume is on)
- Vibration triggers
- Notification lights up (if device supports it)
- Message logged: `d/MyFcmService: Notification sent: ...`

## Debugging

### View Logs

```bash
# All FCM messages
adb logcat -s MyFcmService

# FCM token retrieval
adb logcat -s FCM

# All app logs
adb logcat
```

### Common Issues

**Notification not appearing when app is closed?**
- Ensure the device/emulator is connected and the app is actually running
- Check Firebase console that the message was sent successfully
- Verify notification permissions are granted (Android 13+)

**No sound in emulator?**
- This is expected—emulators typically don't have audio output
- Test on a real device to hear the notification sound

**Token not displaying?**
- Check Logcat for `tag:FCM` to see if there's an error
- Ensure Firebase is properly initialized in `google-services.json`

## Firebase Configuration

The app uses Firebase Cloud Messaging configured via:
- `google-services.json` — Firebase credentials
- `build.gradle.kts` — Firebase BOM and messaging dependency
- `AndroidManifest.xml` — Service registration for `MyFirebaseMessagingService`

Project: `lukelynch-io-sandbox`

## Lifecycle

The foreground/background detection works as follows:

```kotlin
// MainActivity
onResume()  → MyFirebaseMessagingService.setAppInForeground(true)
onPause()   → MyFirebaseMessagingService.setAppInForeground(false)

// MyFirebaseMessagingService
onMessageReceived() {
  if (!isAppInForeground()) {
    sendNotification()  // System notification
  }
  messageStateFlow.value = message  // UI dialog
}
```

This ensures dialogs only appear when the user can see them, while background users always get a system notification.
