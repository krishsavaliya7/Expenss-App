package com.splitsmart.app.data.api.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NotificationData(
    val id: Int,
    val type: String,
    val message: String,
    @Json(name = "is_read") val isRead: Boolean = false,
    @Json(name = "created_at") val createdAt: String? = null,
    @Json(name = "related_id") val relatedId: Int? = null
)

@JsonClass(generateAdapter = true)
data class NotificationsResponse(
    val notifications: List<NotificationData>? = null,
    val error: String? = null
)

@JsonClass(generateAdapter = true)
data class NotificationCountResponse(
    val count: Int = 0,
    val error: String? = null
)

@JsonClass(generateAdapter = true)
data class MarkReadResponse(
    val success: Boolean? = null,
    val message: String? = null,
    val error: String? = null
)

@JsonClass(generateAdapter = true)
data class ReadVisibleRequest(val ids: List<Int>)
