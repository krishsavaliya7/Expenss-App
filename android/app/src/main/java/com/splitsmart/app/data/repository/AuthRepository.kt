package com.splitsmart.app.data.repository

import com.splitsmart.app.data.api.PersistentCookieJar
import com.splitsmart.app.data.api.SplitSmartApi
import com.splitsmart.app.data.api.models.*
import javax.inject.Inject
import javax.inject.Singleton

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String, val code: Int = 0) : Result<Nothing>()
    data object Loading : Result<Nothing>()
}

@Singleton
class AuthRepository @Inject constructor(
    private val api: SplitSmartApi,
    private val cookieJar: PersistentCookieJar
) {
    suspend fun login(loginInput: String, password: String): Result<AuthResponse> = try {
        val response = api.login(LoginRequest(loginInput, password))
        if (response.isSuccessful && response.body()?.success == true) Result.Success(response.body()!!)
        else Result.Error(response.body()?.error ?: "Login failed", response.code())
    } catch (e: Exception) { Result.Error(e.message ?: "Network error") }

    suspend fun signup(email: String, username: String, fullName: String, phoneNumber: String, upiId: String, password: String): Result<AuthResponse> = try {
        val response = api.signup(SignupRequest(email, username, fullName, phoneNumber, upiId, password))
        if (response.isSuccessful && response.body()?.success == true) Result.Success(response.body()!!)
        else Result.Error(response.body()?.error ?: "Signup failed", response.code())
    } catch (e: Exception) { Result.Error(e.message ?: "Network error") }

    suspend fun getMe(): Result<AuthResponse> = try {
        val response = api.getMe()
        if (response.isSuccessful && response.body()?.success == true) Result.Success(response.body()!!)
        else Result.Error("Not authenticated", response.code())
    } catch (e: Exception) { Result.Error(e.message ?: "Network error") }

    suspend fun logout(): Result<AuthResponse> = try {
        val response = api.logout()
        cookieJar.clearCookies()
        if (response.isSuccessful) Result.Success(response.body()!!) else Result.Error("Logout failed")
    } catch (e: Exception) { cookieJar.clearCookies(); Result.Error(e.message ?: "Network error") }

    suspend fun updateProfile(request: UpdateProfileRequest): Result<AuthResponse> = try {
        val response = api.updateProfile(request)
        if (response.isSuccessful && response.body()?.success == true) Result.Success(response.body()!!)
        else Result.Error(response.body()?.error ?: "Update failed", response.code())
    } catch (e: Exception) { Result.Error(e.message ?: "Network error") }
}
