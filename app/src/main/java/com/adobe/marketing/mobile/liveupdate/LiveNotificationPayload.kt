package com.adobe.marketing.mobile.liveupdate

data class LiveNotificationPayload(
    val id: Int,
    val title: String,
    val description: String,
    val step: Step,
    val progressStyle: ProgressStyleConfig? = null,
) {
    enum class Step(val value: String) {
        FIRST("first_step"),
        SECOND("second_step"),
        THIRD("third_step"),
        FORTH("forth_step"),
        FIFTH("fifth_step");

        companion object {
            fun get(value: String): Step {
                return entries.firstOrNull { it.value == value } ?: FIRST
            }
        }
    }
}

data class ProgressStyleConfig(
    val currentProgress: Int,
    val segments: List<SegmentConfig>,
    val points: List<PointConfig>? = null,
    val startIcon: String? = null,
    val endIcon: String? = null,
    val styledByProgress: Boolean = false
)

data class SegmentConfig(
    val length: Int,
    val color: String // Hex color string like "#28A745"
)

data class PointConfig(
    val position: Int,
    val color: String // Hex color string like "#28A745"
)
