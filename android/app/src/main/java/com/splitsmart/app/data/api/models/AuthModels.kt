package com.splitsmart.app.data.api.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginRequest(
    @Json(name = "login_input") val loginInput: String,
    val password: String
)

@JsonClass(generateAdapter = true)
data class SignupRequest(
    val email: String,
    val username: String,
    @Json(name = "full_name") val fullName: String,
    @Json(name = "phone_number") val phoneNumber: String,
    @Json(name = "upi_id") val upiId: String,
    val password: String
)

@JsonClass(generateAdapter = true)
data class AuthResponse(
    val success: Boolean,
    val user: UserData? = null,
    val error: String? = null,
    val message: String? = null
)

@JsonClass(generateAdapter = true)
data class UserData(
    val username: String,
    val email: String,
    @Json(name = "full_name") val fullName: String,
    @Json(name = "phone_number") val phoneNumber: String,
    @Json(name = "upi_id") val upiId: String,
    @Json(name = "profile_pic_url") val profilePicUrl: String? = null
)

@JsonClass(generateAdapter = true)
data class UpdateProfileRequest(
    @Json(name = "full_name") val fullName: String? = null,
    @Json(name = "phone_number") val phoneNumber: String? = null,
    @Json(name = "upi_id") val upiId: String? = null,
    val email: String? = null
)
