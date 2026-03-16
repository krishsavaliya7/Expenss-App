package com.splitsmart.app.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.splitsmart.app.data.api.models.UpdateProfileRequest
import com.splitsmart.app.data.api.models.UserData
import com.splitsmart.app.data.repository.AuthRepository
import com.splitsmart.app.data.repository.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.splitsmart.app.ui.components.AvatarCircle

data class ProfileUiState(val isLoading: Boolean = false, val user: UserData? = null, val isEditing: Boolean = false, val isSaving: Boolean = false, val error: String? = null, val message: String? = null)

@HiltViewModel
class ProfileViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState()); val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()
    init { loadProfile() }
    fun loadProfile() { viewModelScope.launch { _uiState.value = _uiState.value.copy(isLoading = true); when (val r = authRepository.getMe()) { is Result.Success -> _uiState.value = _uiState.value.copy(isLoading = false, user = r.data.user); is Result.Error -> _uiState.value = _uiState.value.copy(isLoading = false, error = r.message); else -> {} } } }
    fun toggleEdit() { _uiState.value = _uiState.value.copy(isEditing = !_uiState.value.isEditing) }
    fun updateProfile(fullName: String, email: String, phone: String, upiId: String) { viewModelScope.launch { _uiState.value = _uiState.value.copy(isSaving = true); when (val r = authRepository.updateProfile(UpdateProfileRequest(fullName, email, phone, upiId))) { is Result.Success -> { _uiState.value = _uiState.value.copy(isSaving = false, isEditing = false, message = "Profile updated"); loadProfile() }; is Result.Error -> _uiState.value = _uiState.value.copy(isSaving = false, error = r.message); else -> {} } } }
    fun logout(onLoggedOut: () -> Unit) { viewModelScope.launch { authRepository.logout(); onLoggedOut() } }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(uiState: ProfileUiState, onToggleEdit: () -> Unit, onSave: (String, String, String, String) -> Unit, onLogout: () -> Unit) {
    var fullName by remember(uiState.user) { mutableStateOf(uiState.user?.fullName ?: "") }
    var email by remember(uiState.user) { mutableStateOf(uiState.user?.email ?: "") }
    var phone by remember(uiState.user) { mutableStateOf(uiState.user?.phoneNumber ?: "") }
    var upiId by remember(uiState.user) { mutableStateOf(uiState.user?.upiId ?: "") }
    var showLogoutDialog by remember { mutableStateOf(false) }

    if (showLogoutDialog) AlertDialog(onDismissRequest = { showLogoutDialog = false }, title = { Text("Logout") }, text = { Text("Are you sure you want to logout?") }, confirmButton = { TextButton(onClick = { showLogoutDialog = false; onLogout() }) { Text("Logout") } }, dismissButton = { TextButton(onClick = { showLogoutDialog = false }) { Text("Cancel") } })

    Scaffold(topBar = { TopAppBar(title = { Text("Profile", fontWeight = FontWeight.Bold) }, actions = { IconButton(onClick = { if (uiState.isEditing) onSave(fullName, email, phone, upiId) else onToggleEdit() }) { Icon(if (uiState.isEditing) Icons.Filled.Save else Icons.Filled.Edit, if (uiState.isEditing) "Save" else "Edit") } }) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(horizontal = 24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.height(24.dp))
            AvatarCircle(uiState.user?.fullName ?: "", 96, uiState.user?.profilePicUrl)
            Spacer(Modifier.height(12.dp))
            Text(uiState.user?.fullName ?: "", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text("@${uiState.user?.username ?: ""}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(32.dp))
            OutlinedTextField(fullName, { fullName = it }, label = { Text("Full Name") }, enabled = uiState.isEditing, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), singleLine = true)
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(email, { email = it }, label = { Text("Email") }, enabled = uiState.isEditing, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), singleLine = true)
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(phone, { phone = it }, label = { Text("Phone") }, enabled = uiState.isEditing, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), singleLine = true)
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(upiId, { upiId = it }, label = { Text("UPI ID") }, enabled = uiState.isEditing, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), singleLine = true)
            Spacer(Modifier.height(32.dp))
            OutlinedButton(onClick = { showLogoutDialog = true }, Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(16.dp), colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)) { Icon(Icons.Filled.Logout, null, Modifier.padding(end = 8.dp)); Text("Logout", style = MaterialTheme.typography.titleMedium) }
            Spacer(Modifier.height(24.dp))
        }
    }
}
