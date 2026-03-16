package com.splitsmart.app.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.splitsmart.app.data.api.models.GroupData
import com.splitsmart.app.data.api.models.UserData
import com.splitsmart.app.data.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DashboardUiState(val isLoading: Boolean = false, val user: UserData? = null, val groups: List<GroupData> = emptyList(), val youOwe: Double = 0.0, val youAreOwed: Double = 0.0, val notificationCount: Int = 0, val error: String? = null)

@HiltViewModel
class DashboardViewModel @Inject constructor(private val authRepository: AuthRepository, private val groupRepository: GroupRepository, private val notificationRepository: NotificationRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()
    init { loadDashboard() }
    fun loadDashboard() { viewModelScope.launch {
        _uiState.value = _uiState.value.copy(isLoading = true)
        when (val r = authRepository.getMe()) { is Result.Success -> _uiState.value = _uiState.value.copy(user = r.data.user); else -> {} }
        when (val r = groupRepository.getGroups()) { is Result.Success -> { val groups = r.data.groups ?: emptyList(); var owe = 0.0; var owed = 0.0; groups.forEach { val b = it.balance ?: 0.0; if (b < 0) owe += kotlin.math.abs(b) else if (b > 0) owed += b }; _uiState.value = _uiState.value.copy(groups = groups, youOwe = owe, youAreOwed = owed) }; else -> {} }
        when (val r = notificationRepository.getNotificationCount()) { is Result.Success -> _uiState.value = _uiState.value.copy(notificationCount = r.data.count); else -> {} }
        _uiState.value = _uiState.value.copy(isLoading = false)
    }}
}
