package com.splitsmart.app.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.splitsmart.app.data.api.models.UserData
import com.splitsmart.app.data.repository.AuthRepository
import com.splitsmart.app.data.repository.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(val isLoading: Boolean = false, val isLoggedIn: Boolean = false, val user: UserData? = null, val error: String? = null, val signupSuccess: Boolean = false)

@HiltViewModel
class AuthViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init { checkAuthStatus() }

    fun checkAuthStatus() { viewModelScope.launch {
        _uiState.value = _uiState.value.copy(isLoading = true)
        when (val r = authRepository.getMe()) {
            is Result.Success -> _uiState.value = _uiState.value.copy(isLoading = false, isLoggedIn = true, user = r.data.user)
            is Result.Error -> _uiState.value = _uiState.value.copy(isLoading = false, isLoggedIn = false)
            is Result.Loading -> {}
        }
    }}

    fun login(loginInput: String, password: String) { viewModelScope.launch {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        when (val r = authRepository.login(loginInput, password)) {
            is Result.Success -> _uiState.value = _uiState.value.copy(isLoading = false, isLoggedIn = true, user = r.data.user, error = null)
            is Result.Error -> _uiState.value = _uiState.value.copy(isLoading = false, error = r.message)
            is Result.Loading -> {}
        }
    }}

    fun signup(email: String, username: String, fullName: String, phoneNumber: String, upiId: String, password: String) { viewModelScope.launch {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        when (val r = authRepository.signup(email, username, fullName, phoneNumber, upiId, password)) {
            is Result.Success -> _uiState.value = _uiState.value.copy(isLoading = false, signupSuccess = true, error = null)
            is Result.Error -> _uiState.value = _uiState.value.copy(isLoading = false, error = r.message)
            is Result.Loading -> {}
        }
    }}

    fun logout() { viewModelScope.launch { authRepository.logout(); _uiState.value = AuthUiState(isLoggedIn = false) } }
    fun checkAuth() { checkAuthStatus() }
    fun clearError() { _uiState.value = _uiState.value.copy(error = null) }
    fun clearSignupSuccess() { _uiState.value = _uiState.value.copy(signupSuccess = false) }
}
