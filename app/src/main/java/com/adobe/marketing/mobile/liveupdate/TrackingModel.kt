package com.adobe.marketing.mobile.liveupdate

import android.app.Notification

data class TrackingModel(
    val timeNeeded: Long,
    val builder:  Notification.Builder
)
