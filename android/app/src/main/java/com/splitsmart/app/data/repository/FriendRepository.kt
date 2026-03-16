package com.splitsmart.app.data.repository

import com.splitsmart.app.data.api.SplitSmartApi
import com.splitsmart.app.data.api.models.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FriendRepository @Inject constructor(private val api: SplitSmartApi) {
    suspend fun searchUsers(searchTerm: String): Result<SearchUsersResponse> = try {
        val r = api.searchUsers(SearchUsersRequest(searchTerm)); if (r.isSuccessful) Result.Success(r.body()!!) else Result.Error("Search failed", r.code())
    } catch (e: Exception) { Result.Error(e.message ?: "Network error") }

    suspend fun sendFriendRequest(receiverName: String): Result<FriendRequestResponse> = try {
        val r = api.sendFriendRequest(FriendRequestBody(receiverName)); if (r.isSuccessful) Result.Success(r.body()!!) else Result.Error(r.body()?.error ?: "Failed", r.code())
    } catch (e: Exception) { Result.Error(e.message ?: "Network error") }

    suspend fun acceptFriendRequest(senderName: String): Result<FriendRequestResponse> = try {
        val r = api.acceptFriendRequest(AcceptRejectRequest(senderName)); if (r.isSuccessful) Result.Success(r.body()!!) else Result.Error(r.body()?.error ?: "Failed", r.code())
    } catch (e: Exception) { Result.Error(e.message ?: "Network error") }

    suspend fun rejectFriendRequest(senderName: String): Result<FriendRequestResponse> = try {
        val r = api.rejectFriendRequest(AcceptRejectRequest(senderName)); if (r.isSuccessful) Result.Success(r.body()!!) else Result.Error(r.body()?.error ?: "Failed", r.code())
    } catch (e: Exception) { Result.Error(e.message ?: "Network error") }

    suspend fun getFriends(): Result<FriendsListResponse> = try {
        val r = api.getFriends(); if (r.isSuccessful) Result.Success(r.body()!!) else Result.Error("Failed", r.code())
    } catch (e: Exception) { Result.Error(e.message ?: "Network error") }
}
