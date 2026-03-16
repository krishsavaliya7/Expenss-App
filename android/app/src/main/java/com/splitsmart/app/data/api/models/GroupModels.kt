package com.splitsmart.app.data.api.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GroupData(
    @Json(name = "group_id") val groupId: Int,
    @Json(name = "group_name") val groupName: String,
    val description: String? = null,
    val currency: String = "INR",
    @Json(name = "created_by") val createdBy: String,
    @Json(name = "created_at") val createdAt: String? = null,
    @Json(name = "invite_token") val inviteToken: String? = null,
    @Json(name = "member_count") val memberCount: Int? = null,
    val balance: Double? = null
)

@JsonClass(generateAdapter = true)
data class GroupListResponse(
    val groups: List<GroupData>? = null,
    val error: String? = null
)

@JsonClass(generateAdapter = true)
data class CreateGroupRequest(
    @Json(name = "group_name") val groupName: String,
    val description: String? = null,
    val currency: String = "INR",
    @Json(name = "initial_members") val members: List<String>? = null
)

@JsonClass(generateAdapter = true)
data class CreateGroupResponse(
    val success: Boolean? = null,
    @Json(name = "group_id") val groupId: Int? = null,
    val message: String? = null,
    val error: String? = null
)

@JsonClass(generateAdapter = true)
data class GroupDetailResponse(
    val group: GroupData? = null,
    val members: List<MemberData>? = null,
    val error: String? = null
)

@JsonClass(generateAdapter = true)
data class MemberData(
    val username: String,
    @Json(name = "full_name") val fullName: String? = null,
    val role: String? = null,
    @Json(name = "profile_pic_url") val profilePicUrl: String? = null
)

@JsonClass(generateAdapter = true)
data class MemberSuggestion(
    val username: String,
    @Json(name = "full_name") val fullName: String? = null,
    @Json(name = "profile_pic_url") val profilePicUrl: String? = null
)

@JsonClass(generateAdapter = true)
data class MemberSuggestionsResponse(
    val suggestions: List<MemberSuggestion>? = null,
    val error: String? = null
)

@JsonClass(generateAdapter = true)
data class AddMemberRequest(val username: String)

@JsonClass(generateAdapter = true)
data class JoinGroupResponse(
    val success: Boolean? = null,
    val message: String? = null,
    val error: String? = null,
    @Json(name = "group_id") val groupId: Int? = null
)
