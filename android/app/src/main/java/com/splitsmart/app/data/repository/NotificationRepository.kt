package com.splitsmart.app.data.repository

import com.splitsmart.app.data.api.SplitSmartApi
import com.splitsmart.app.data.api.models.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepository @Inject constructor(private val api: SplitSmartApi) {
    suspend fun getNotifications(limit: Int = 50, offset: Int = 0): Result<NotificationsResponse> = try {
        val r = api.getNotifications(limit, offset); if (r.isSuccessful) Result.Success(r.body()!!) else Result.Error("Failed", r.code())
    } catch (e: Exception) { Result.Error(e.message ?: "Network error") }

    suspend fun getNotificationCount(): Result<NotificationCountResponse> = try {
        val r = api.getNotificationCount(); if (r.isSuccessful) Result.Success(r.body()!!) else Result.Error("Failed", r.code())
    } catch (e: Exception) { Result.Error(e.message ?: "Network error") }

    suspend fun markAllRead(): Result<MarkReadResponse> = try {
        val r = api.markAllNotificationsRead(); if (r.isSuccessful) Result.Success(r.body()!!) else Result.Error("Failed", r.code())
    } catch (e: Exception) { Result.Error(e.message ?: "Network error") }

    suspend fun markVisibleRead(ids: List<Int>): Result<MarkReadResponse> = try {
        val r = api.markVisibleNotificationsRead(ReadVisibleRequest(ids)); if (r.isSuccessful) Result.Success(r.body()!!) else Result.Error("Failed", r.code())
    } catch (e: Exception) { Result.Error(e.message ?: "Network error") }
}
