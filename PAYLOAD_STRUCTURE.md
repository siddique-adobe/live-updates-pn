# Live Notification Payload Structure

This document describes the enhanced payload structure for the Live Notification Android app that supports dynamic `Notification.ProgressStyle` configuration via FCM.

## Overview

The new payload structure allows you to send rich progress-style notifications with customizable segments, points, colors, and icons through Firebase Cloud Messaging (FCM).

## Payload Structure

### Basic Structure

```json
{
  "id": "123",
  "type": "LIVE_NOTIFICATION",
  "title": "Route Accepted",
  "description": "Driver is on the way to you",
  "step": "first_step",
  "progressStyle": {
    "currentProgress": 0,
    "styledByProgress": false,
    "segments": [
      { "length": 50, "color": "#28A745" },
      { "length": 50, "color": "#8BC598" },
      { "length": 50, "color": "#8BC598" }
    ],
    "points": [
      { "position": 50, "color": "#28A745" },
      { "position": 100, "color": "#8BC598" }
    ],
    "startIcon": "driver",
    "endIcon": "location_pin"
  }
}
```

### Field Descriptions

#### Required Fields
- `id`: Unique identifier for the notification (integer)
- `type`: Must be "LIVE_NOTIFICATION" (string)
- `title`: Notification title (string)
- `description`: Notification description (string)
- `step`: Step identifier - "first_step", "second_step", or "third_step" (string)

#### Optional Fields
- `progressStyle`: Progress style configuration object (optional - if not provided, a simple notification will be shown)

### ProgressStyle Configuration

#### Required Fields
- `currentProgress`: Current progress value (0-150) (integer)
- `segments`: Array of segment configurations (array)

#### Optional Fields
- `styledByProgress`: Whether to use progress-based styling (boolean, default: false)
- `points`: Array of point configurations (array, optional)
- `startIcon`: Icon name for the start of the progress bar (string, optional)
- `endIcon`: Icon name for the end of the progress bar (string, optional)

### Segment Configuration
```json
{
  "length": 50,
  "color": "#28A745"
}
```
- `length`: Length of the segment (integer)
- `color`: Hex color code for the segment (string)

### Point Configuration
```json
{
  "position": 50,
  "color": "#28A745"
}
```
- `position`: Position of the point on the progress bar (integer)
- `color`: Hex color code for the point (string)

## Available Icons

The following icon names are supported:
- `"driver"`: Driver icon
- `"location_pin"`: Location pin icon

## Examples

### Example 1: Segment-only Progress Style
```json
{
  "id": "123",
  "type": "LIVE_NOTIFICATION",
  "title": "Route Accepted",
  "description": "Driver is on the way to you",
  "step": "first_step",
  "progressStyle": {
    "currentProgress": 0,
    "styledByProgress": false,
    "segments": [
      { "length": 50, "color": "#28A745" },
      { "length": 50, "color": "#8BC598" },
      { "length": 50, "color": "#8BC598" }
    ],
    "startIcon": "driver",
    "endIcon": "location_pin"
  }
}
```

### Example 2: Segment with Progress Points
```json
{
  "id": "124",
  "type": "LIVE_NOTIFICATION",
  "title": "Driver arrived",
  "description": "Driver is waiting for you",
  "step": "second_step",
  "progressStyle": {
    "currentProgress": 75,
    "styledByProgress": false,
    "segments": [
      { "length": 50, "color": "#28A745" },
      { "length": 50, "color": "#28A745" },
      { "length": 50, "color": "#8BC598" }
    ],
    "points": [
      { "position": 50, "color": "#28A745" },
      { "position": 100, "color": "#8BC598" }
    ],
    "startIcon": "driver",
    "endIcon": "location_pin"
  }
}
```

### Example 3: Simple Notification (No Progress Style)
```json
{
  "id": "125",
  "type": "LIVE_NOTIFICATION",
  "title": "Simple Notification",
  "description": "This is a simple notification without progress style",
  "step": "third_step"
}
```

## FCM Implementation

### JavaScript (Node.js)
```javascript
const admin = require('firebase-admin');

let message = {
  data: {
    id: "123",
    type: "LIVE_NOTIFICATION",
    title: 'Route Accepted',
    description: 'Driver is on the way to you',
    step: 'first_step',
    progressStyle: JSON.stringify({
      currentProgress: 0,
      styledByProgress: false,
      segments: [
        { length: 50, color: "#28A745" },
        { length: 50, color: "#8BC598" },
        { length: 50, color: "#8BC598" }
      ],
      startIcon: "driver",
      endIcon: "location_pin"
    })
  },
  topic: 'live_notification'
};

admin.messaging().send(message);
```

### Android Parsing

The Android app automatically parses the `progressStyle` JSON string and creates the appropriate `Notification.ProgressStyle` configuration. If parsing fails, the notification will be shown without progress styling.

## Error Handling

- If `progressStyle` is missing or null, a simple notification will be shown
- If JSON parsing fails, the notification will be shown without progress styling
- Invalid icon names will fall back to default icons
- Invalid color codes will cause the notification to fail gracefully

## Requirements

- Android 16+ (API level 36) for progress-style notifications
- Firebase Cloud Messaging setup
- Proper notification permissions granted

## Notes

- Progress-style notifications are only supported on Android 16+ devices
- On older devices, notifications will be shown without progress styling
- The `progressStyle` field must be a valid JSON string when sent via FCM
- Color codes should be in hex format (e.g., "#28A745")
- Segment lengths should add up to a reasonable total (typically 150)
- Point positions should be within the range of your progress bar 