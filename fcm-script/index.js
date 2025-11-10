const admin = require('firebase-admin');

admin.initializeApp({
  credential: admin.credential.cert('./serviceAccountKey.json'),
});

// STEP 1: Segment-only progress style
let stepOneMessage = {
  data: {
    id: "123",
    type: "LIVE_NOTIFICATION",
    title: 'Route Accepted',
    description: 'Driver will be allocated shortly',
    step: 'first_step',
    progressStyle: JSON.stringify({
      currentProgress: 0,
      styledByProgress: false,
      segments: [
        { length: 25, color: "#939694" },
        { length: 25, color: "#939694" },
        { length: 25, color: "#939694" },
        { length: 25, color: "#939694" }
      ],
      points: [
        { position: 25, color: "#939694" },
        { position: 50, color: "#939694" },
        { position: 75, color: "#939694" }
      ],
      startIcon: "driver",
      endIcon: "location_pin"
    })
  },
  topic: 'live_notification'
};

// STEP 2: Segment with progress points (COMMENTED OUT - uncomment when ready to test)

let stepTwoMessage = {
  data: {
    id: "123",
    type: "LIVE_NOTIFICATION",
    title: 'Heading towards you',
    description: 'Driver has started the journey',
    step: 'second_step',
    progressStyle: JSON.stringify({
      currentProgress: 25,
      styledByProgress: false,
      segments: [
        { length: 25, color: "#28A745" },
        { length: 25, color: "#939694" },
        { length: 25, color: "#939694" },
        { length: 25, color: "#939694" }
      ],
      points: [
        { position: 25, color: "#28A745" },
        { position: 50, color: "#939694" },
        { position: 75, color: "#939694" }
      ],
      startIcon: "driver",
      endIcon: "location_pin"
    })
  },
  topic: 'live_notification'
};

// STEP 3: Simple notification without progress style (COMMENTED OUT - uncomment when ready to test)

let stepThreeMessage = {
  data: {
    id: "123",
    type: "LIVE_NOTIFICATION",
    title: 'Half way there',
    description: 'Driver is in the middle of the journey',
    step: 'third_step',
    progressStyle: JSON.stringify({
      currentProgress: 50,
      styledByProgress: false,
      segments: [
        { length: 25, color: "#28A745" },
        { length: 25, color: "#28A745" },
        { length: 25, color: "#939694" },
        { length: 25, color: "#939694" }
      ],
      points: [
        { position: 25, color: "#28A745" },
        { position: 50, color: "#28A745" },
        { position: 75, color: "#939694" }
      ],
      startIcon: "driver",
      endIcon: "location_pin"
    })
  },
  topic: 'live_notification'
};

// STEP 4: Simple notification without progress style (COMMENTED OUT - uncomment when ready to test)

let stepFourMessage = {
  data: {
    id: "123",
    type: "LIVE_NOTIFICATION",
    title: 'Hurry Up',
    description: 'Driver is about to reach you',
    step: 'forth_step',
    progressStyle: JSON.stringify({
      currentProgress: 75,
      styledByProgress: false,
      segments: [
        { length: 25, color: "#28A745" },
        { length: 25, color: "#28A745" },
        { length: 25, color: "#28A745" },
        { length: 25, color: "#939694" }
      ],
      points: [
        { position: 25, color: "#28A745" },
        { position: 50, color: "#28A745" },
        { position: 75, color: "#28A745" }
      ],
      startIcon: "driver",
      endIcon: "location_pin"
    })
  },
  topic: 'live_notification'
};

// STEP 5: Simple notification without progress style (COMMENTED OUT - uncomment when ready to test)

let stepFiveMessage = {
  data: {
    id: "123",
    type: "LIVE_NOTIFICATION",
    title: 'Driver arrived',
    description: 'Driver is waiting for you',
    step: 'fifth_step',
    progressStyle: JSON.stringify({
      currentProgress: 100,
      styledByProgress: false,
      segments: [
        { length: 25, color: "#28A745" },
        { length: 25, color: "#28A745" },
        { length: 25, color: "#28A745" },
        { length: 25, color: "#28A745" }
      ],
      points: [
        { position: 25, color: "#28A745" },
        { position: 50, color: "#28A745" },
        { position: 75, color: "#28A745" }
      ],
      startIcon: "driver",
      endIcon: "location_pin"
    })
  },
  topic: 'live_notification'
};

console.log('🚀 Sending STEP 1: Segment-only progress style notification...');

// Send STEP 1: Segment-only message
admin.messaging().send(stepOneMessage)
  .then((response) => {
    console.log('✅ Successfully sent STEP 1 (segment-only):', response);
    console.log('📱 Check your device for the notification');
  })
  .catch((error) => {
    console.log('❌ Error sending STEP 1:', error);
  });

/*
// To test STEP 2, uncomment this section:
console.log('🚀 Sending STEP 2: Segment with progress points notification...');
admin.messaging().send(stepTwoMessage)
  .then((response) => {
    console.log('✅ Successfully sent STEP 2 (segment with points):', response);
    console.log('📱 Check your device for the notification');
  })
  .catch((error) => {
    console.log('❌ Error sending STEP 2:', error);
  });

// To test STEP 3, uncomment this section:
console.log('🚀 Sending STEP 3: Simple notification...');
admin.messaging().send(stepThreeMessage)
  .then((response) => {
    console.log('✅ Successfully sent STEP 3 (simple):', response);
    console.log('📱 Check your device for the notification');
  })
  .catch((error) => {
    console.log('❌ Error sending STEP 3:', error);
  });
*/
