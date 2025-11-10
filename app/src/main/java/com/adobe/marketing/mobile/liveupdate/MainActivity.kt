package com.adobe.marketing.mobile.liveupdate

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.adobe.marketing.mobile.liveupdate.LiveNotificationManager.NOTIFICATION_TOPIC
import com.adobe.marketing.mobile.liveupdate.ui.theme.LiveNotificationAndroidTheme
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : ComponentActivity() {
    
    companion object {
        private const val TAG = "MainActivity"
    }
    
    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Log.d(TAG, "✅ Notification permission granted")
            Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show()
        } else {
            Log.d(TAG, "❌ Notification permission denied")
            Toast.makeText(this, "Notification permissions are denied", Toast.LENGTH_LONG).show()
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        LiveNotificationManager.createNotificationChannel(this)
        
        // Request notification permission for Android 13+
        requestNotificationPermission()
        
        // Get and log FCM token
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                Log.d(TAG, "🔑 FCM Token: $token")
                Log.d(TAG, "📝 Copy this token if you want to send direct messages instead of topic messages")
            } else {
                Log.e(TAG, "❌ Failed to get FCM token", task.exception)
            }
        }

        setContent {
            LiveNotificationAndroidTheme {
                MainScreen()
            }
        }
    }
    
    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    Log.d(TAG, "✅ Notification permission already granted")
                }
                shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                    Log.d(TAG, "📝 Showing permission rationale")
                    Toast.makeText(
                        this,
                        "Notification permission is needed to receive live updates",
                        Toast.LENGTH_LONG
                    ).show()
                    notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
                else -> {
                    Log.d(TAG, "🔔 Requesting notification permission")
                    notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        } else {
            Log.d(TAG, "ℹ️ Notification permission not required for API level < 33")
        }
    }
}

@Composable
fun MainScreen() {
    var subscribed by rememberSaveable {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedContent(
            targetState = subscribed,
            label = ""
        ) { target ->
            when {
                target -> SubscribedUI()

                else -> {
                    Button(
                        onClick = {
                            Log.d("MainActivity", "🔔 Subscribing to topic: $NOTIFICATION_TOPIC")
                            FirebaseMessaging.getInstance().subscribeToTopic(NOTIFICATION_TOPIC)
                                .addOnSuccessListener {
                                    Log.d("MainActivity", "✅ Successfully subscribed to topic: $NOTIFICATION_TOPIC")
                                    subscribed = true
                                }
                                .addOnFailureListener { exception ->
                                    Log.e("MainActivity", "❌ Failed to subscribe to topic", exception)
                                }
                        }
                    ) {
                        Text(text = "Subscribe to topic")
                    }
                }
            }
        }
    }
}

@Composable
fun SubscribedUI() {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text = "Subscribed!",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Run the script from the 'fcm-script' folder to create your live notification",
            style = MaterialTheme.typography.headlineSmall.copy(
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Normal
            )
        )
    }
}

@Preview
@Composable
fun SubscribedUIPreview() {
    SubscribedUI()
}