package com.adobe.marketing.mobile.liveupdate

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.content.Context
import android.graphics.drawable.Icon
import android.os.Build
import androidx.core.graphics.toColorInt
import com.adobe.marketing.mobile.liveupdate.utils.notificationManager


object LiveNotificationManager {
    private const val NOTIFICATION_TAG = "live_notification_tag"
    private lateinit var notificationManager: NotificationManager
    private lateinit var appContext: Context

    const val CHANNEL_ID = "live_updates_channel_id"
    const val NOTIFICATION_TOPIC = "live_notification"
    private const val CHANNEL_NAME = "live_updates_channel_name"

    fun createNotificationChannel(context: Context) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        this.notificationManager = notificationManager
        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, IMPORTANCE_DEFAULT)
        appContext = context
        this.notificationManager.createNotificationChannel(channel)
    }

    fun showPointsLiveNotification(payload: LiveNotificationPayload): Notification.Builder {
        val builder = Notification.Builder(appContext, CHANNEL_ID).apply {
            setSmallIcon(R.drawable.ic_launcher_foreground)
            setContentTitle(payload.title)
            setContentText(payload.description)
            setAutoCancel(true)

            // Building ProgressStyle from payload
            val progressStyle: Notification.ProgressStyle? = payload.progressStyle?.let { config ->
                buildProgressStyleFromConfig(config)
            }
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BAKLAVA && progressStyle != null) {
                setStyle(progressStyle)
            }
        }
        
        return builder
    }

    fun showNotification(
        context: Context,
        payload: LiveNotificationPayload
    ) {
        val notificationBuilder = showPointsLiveNotification(payload)
        val notification = notificationBuilder.build()

        context.notificationManager.notify(
            NOTIFICATION_TAG,
            payload.id,
            notification
        )
    }

    private fun buildProgressStyleFromConfig(config: ProgressStyleConfig): Notification.ProgressStyle? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BAKLAVA) {
            // Creating the progress segments style from config
            val progressSegmentList: List<Notification.ProgressStyle.Segment> =
                config.segments.map { segment ->
                    Notification.ProgressStyle.Segment(segment.length)
                        .setColor(segment.color.toColorInt())
                }

            Notification.ProgressStyle().apply {
                setStyledByProgress(config.styledByProgress)
                setProgress(config.currentProgress)
                setProgressSegments(progressSegmentList)
                
                // Set start icon if provided
                config.startIcon?.let { iconName ->
                    val iconResId = getIconResourceId(iconName)
                    if (iconResId != 0) {
                        setProgressStartIcon(Icon.createWithResource(appContext, iconResId))
                    }
                } ?: run {
                    // Default start icon
                    setProgressStartIcon(Icon.createWithResource(appContext, R.drawable.ic_route))
                }
                
                // Set end icon if provided
                config.endIcon?.let { iconName ->
                    val iconResId = getIconResourceId(iconName)
                    if (iconResId != 0) {
                        setProgressEndIcon(Icon.createWithResource(appContext, iconResId))
                    }
                } ?: run {
                    // Default end icon
                    setProgressEndIcon(Icon.createWithResource(appContext, R.drawable.ic_destination))
                }
                
                // Creating the progress points style if provided
                config.points?.let { points ->
                    val progressPointList = points.map { point ->
                        Notification.ProgressStyle.Point(point.position)
                            .setColor(point.color.toColorInt())
                    }
                    setProgressPoints(progressPointList)
                }
                setProgressTrackerIcon(Icon.createWithResource(appContext,R.drawable.ic_transit))
            }
        } else null
    }

    private fun getIconResourceId(iconName: String): Int {
        return when (iconName.lowercase()) {
            "driver" -> R.drawable.ic_route
            "location_pin" -> R.drawable.ic_destination
            else -> 0
        }
    }
}