package com.adobe.marketing.mobile.liveupdate

import android.annotation.SuppressLint
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random
import org.json.JSONArray
import org.json.JSONObject

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class FCMService : FirebaseMessagingService() {

    companion object {
        private const val TAG = "FCMService"
        private const val NOTIFICATION_TYPE = "type"
        private const val LIVE_NOTIFICATION = "LIVE_NOTIFICATION"
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        
        Log.d(TAG, "📨 FCM message received: ${message.messageId}")
        Log.d(TAG, "📋 Message data: ${message.data}")
        Log.d(TAG, "📋 Message data size: ${message.data.size}")
        Log.d(TAG, "📋 Message notification: ${message.notification}")
        Log.d(TAG, "📋 Message from: ${message.from}")
        
        // Check if data is empty
        if (message.data.isEmpty()) {
            Log.e(TAG, "❌ ERROR: message.data is EMPTY!")
            Log.e(TAG, "This usually means:")
            Log.e(TAG, "1. The device is not subscribed to the topic")
            Log.e(TAG, "2. The FCM token is invalid")
            Log.e(TAG, "3. The message was sent with a 'notification' payload (use data-only)")
            return
        }

        val data: Map<String, Any> = message.data
        
        Log.d(TAG, "📋 Data contents:")
        data.forEach { (key, value) ->
            Log.d(TAG, "   - $key: $value")
        }

        if (data[NOTIFICATION_TYPE] == LIVE_NOTIFICATION) {
            Log.d(TAG, "✅ Processing LIVE_NOTIFICATION message")
            
            val payload = LiveNotificationPayload(
                id = data["id"].toString().toIntOrNull() ?: Random.nextInt(),
                title = data["title"].toString(),
                description = data["description"].toString(),
                step = LiveNotificationPayload.Step.get(data["step"].toString()),
                progressStyle = parseProgressStyle(data["progressStyle"]?.toString())
            )
            
            Log.d(TAG, "📦 Created payload: $payload")
            
            LiveNotificationManager.showNotification(
                context = this,
                payload = payload
            )
            
            Log.d(TAG, "✅ Notification displayed successfully")
        } else {
            Log.d(TAG, "❌ Unknown notification type: ${data[NOTIFICATION_TYPE]}")
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "🆕 New FCM token: $token")
    }

    private fun parseProgressStyle(progressStyleJson: String?): ProgressStyleConfig? {
        if (progressStyleJson.isNullOrEmpty()) {
            Log.d(TAG, "📝 No progress style provided, using simple notification")
            return null
        }
        
        Log.d(TAG, "📝 Parsing progress style: $progressStyleJson")
        
        return try {
            val json = JSONObject(progressStyleJson)
            
            // Parse segments
            val segmentsArray = json.getJSONArray("segments")
            val segments = mutableListOf<SegmentConfig>()
            for (i in 0 until segmentsArray.length()) {
                val segmentObj = segmentsArray.getJSONObject(i)
                segments.add(
                    SegmentConfig(
                        length = segmentObj.getInt("length"),
                        color = segmentObj.getString("color")
                    )
                )
            }
            
            // Parse points (optional)
            val points = if (json.has("points") && !json.isNull("points")) {
                val pointsArray = json.getJSONArray("points")
                val pointsList = mutableListOf<PointConfig>()
                for (i in 0 until pointsArray.length()) {
                    val pointObj = pointsArray.getJSONObject(i)
                    pointsList.add(
                        PointConfig(
                            position = pointObj.getInt("position"),
                            color = pointObj.getString("color")
                        )
                    )
                }
                pointsList
            } else null
            
            val config = ProgressStyleConfig(
                currentProgress = json.getInt("currentProgress"),
                segments = segments,
                points = points,
                startIcon = json.optString("startIcon").takeIf { it.isNotEmpty() },
                endIcon = json.optString("endIcon").takeIf { it.isNotEmpty() },
                styledByProgress = json.optBoolean("styledByProgress", false)
            )
            
            Log.d(TAG, "✅ Successfully parsed progress style: $config")
            config
        } catch (e: Exception) {
            // Log error and return null if parsing fails
            Log.e(TAG, "❌ Error parsing progress style: ${e.message}", e)
            null
        }
    }
}