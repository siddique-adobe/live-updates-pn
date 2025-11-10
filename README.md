# Live Updates PN

An Android application that displays live progress notifications using Firebase Cloud Messaging (FCM).

## Features

- 🔔 Real-time push notifications with live progress tracking
- 📊 Segmented progress visualization
- 🎨 Customizable progress styles with colors
- 📍 Start/End icons and progress tracker
- 🔄 Dynamic notification updates

## Screenshots

![Segment Style](screenshots/live_notification_android_segment_style.gif)
![Segment Point Style](screenshots/live_notification_android_segment_point_style.gif)

## Prerequisites

- Android Studio (latest version recommended)
- Android SDK 28 or higher
- Firebase account
- Node.js (for FCM script)

## Setup Instructions

### 1. Firebase Configuration

1. Create a new Firebase project at [Firebase Console](https://console.firebase.google.com/)
2. Add an Android app with package name: `com.adobe.marketing.mobile.liveupdate`
3. Download `google-services.json` and place it in `app/` directory
4. For FCM script, download service account key:
   - Go to Project Settings → Service Accounts
   - Generate new private key
   - Save as `fcm-script/serviceAccountKey.json`

### 2. Install Dependencies

```bash
# Install Node.js dependencies for FCM script
cd fcm-script
npm install
```

### 3. Build and Run

```bash
# Build the Android app
./gradlew assembleDebug

# Install on connected device
./gradlew installDebug
```

### 4. Send Test Notifications

```bash
# Run the FCM script to send notifications
cd fcm-script
node index.js
```

## Project Structure

```
├── app/
│   ├── src/main/java/com/adobe/marketing/mobile/liveupdate/
│   │   ├── MainActivity.kt              # Main activity with permission handling
│   │   ├── FCMService.kt                # Firebase messaging service
│   │   ├── LiveNotificationManager.kt   # Notification builder
│   │   ├── LiveNotificationPayload.kt   # Data models
│   │   └── ui/theme/                    # UI theme configuration
│   ├── google-services.json             # Firebase config (excluded from git)
│   └── build.gradle.kts
├── fcm-script/
│   ├── index.js                         # FCM notification sender
│   ├── serviceAccountKey.json           # Firebase admin key (excluded from git)
│   └── package.json
└── PAYLOAD_STRUCTURE.md                 # Notification payload documentation
```

## Notification Payload Structure

See [PAYLOAD_STRUCTURE.md](PAYLOAD_STRUCTURE.md) for detailed information about the notification data structure.

## Key Features Implementation

### Progress Notification Styles

- **Segment Style**: Visual progress segments with custom colors
- **Progress Points**: Milestone indicators along the progress bar
- **Dynamic Icons**: Start, end, and tracker icons

### Permission Handling

- Automatic notification permission request (Android 13+)
- Toast notifications for permission status
- Graceful fallback for older Android versions

### Firebase Integration

- Topic-based messaging
- Data-only notifications (no notification payload)
- Real-time message processing

## Requirements

- **Min SDK**: 28 (Android 9.0)
- **Target SDK**: 36 (Android 16)
- **Compile SDK**: 36

## Dependencies

- Firebase Cloud Messaging (BOM)
- Jetpack Compose
- AndroidX Core KTX
- Material 3

## License

This project is for demonstration purposes.

## Contributing

Feel free to submit issues and enhancement requests!
