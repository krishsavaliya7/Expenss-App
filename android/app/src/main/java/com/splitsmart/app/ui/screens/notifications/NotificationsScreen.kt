package com.splitsmart.app.ui.screens.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.splitsmart.app.data.api.models.NotificationData
import com.splitsmart.app.data.repository.NotificationRepository
import com.splitsmart.app.data.repository.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.splitsmart.app.ui.components.*

data class NotificationsUiState(val isLoading: Boolean = false, val notifications: List<NotificationData> = emptyList(), val error: String? = null)

@HiltViewModel
class NotificationsViewModel @Inject constructor(private val notificationRepository: NotificationRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(NotificationsUiState()); val uiState: StateFlow<NotificationsUiState> = _uiState.asStateFlow()
    init { loadNotifications() }
    fun loadNotifications() { viewModelScope.launch { _uiState.value = _uiState.value.copy(isLoading = true); when (val r = notificationRepository.getNotifications()) { is Result.Success -> _uiState.value = _uiState.value.copy(isLoading = false, notifications = r.data.notifications ?: emptyList()); is Result.Error -> _uiState.value = _uiState.value.copy(isLoading = false, error = r.message); else -> {} } } }
    fun markAllRead() { viewModelScope.launch { when (notificationRepository.markAllRead()) { is Result.Success -> loadNotifications(); else -> {} } } }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(uiState: NotificationsUiState, onMarkAllRead: () -> Unit, onBack: () -> Unit, onRefresh: () -> Unit) {
    Scaffold(topBar = { TopAppBar(title = { Text("Notifications", fontWeight = FontWeight.Bold) }, navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } }, actions = { if (uiState.notifications.isNotEmpty()) IconButton(onClick = onMarkAllRead) { Icon(Icons.Filled.DoneAll, "Mark all read") } }) }) { padding ->
        if (uiState.isLoading) ShimmerList(modifier = Modifier.padding(padding))
        else if (uiState.notifications.isEmpty()) EmptyState(Icons.Outlined.Notifications, "No notifications", "You're all caught up!", Modifier.padding(padding))
        else LazyColumn(Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            item { Spacer(Modifier.height(4.dp)) }
            items(uiState.notifications) { NotificationItem(it) }
            item { Spacer(Modifier.height(24.dp)) }
        }
    }
}
