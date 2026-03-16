package com.splitsmart.app.data.api.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FriendData(
    val username: String,
    @Json(name = "full_name") val fullName: String? = null,
    @Json(name = "profile_pic_url") val profilePicUrl: String? = null,
    @Json(name = "request_status") val requestStatus: String? = null
)

@JsonClass(generateAdapter = true)
data class FriendsListResponse(
    val friends: List<FriendData>? = null,
    val error: String? = null
)

@JsonClass(generateAdapter = true)
data class SearchUsersRequest(
    @Json(name = "search_term") val searchTerm: String
)

@JsonClass(generateAdapter = true)
data class SearchUsersResponse(
    val results: List<FriendData>? = null,
    val error: String? = null
)

@JsonClass(generateAdapter = true)
data class FriendRequestBody(
    @Json(name = "receiver_name") val receiverName: String
)

@JsonClass(generateAdapter = true)
data class FriendRequestResponse(
    val success: Boolean? = null,
    val message: String? = null,
    val error: String? = null
)

@JsonClass(generateAdapter = true)
data class AcceptRejectRequest(
    @Json(name = "sender_name") val senderName: String
)
