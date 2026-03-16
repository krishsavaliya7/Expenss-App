package com.splitsmart.app.ui.screens.friends

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.splitsmart.app.data.api.models.FriendData
import com.splitsmart.app.data.repository.FriendRepository
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.People
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.splitsmart.app.ui.components.*

data class FriendsUiState(val isLoading: Boolean = false, val friends: List<FriendData> = emptyList(), val searchResults: List<FriendData> = emptyList(), val isSearching: Boolean = false, val error: String? = null, val actionMessage: String? = null)

@HiltViewModel
class FriendsViewModel @Inject constructor(private val friendRepository: FriendRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(FriendsUiState()); val uiState: StateFlow<FriendsUiState> = _uiState.asStateFlow()
    init { loadFriends() }
    fun loadFriends() { viewModelScope.launch { _uiState.value = _uiState.value.copy(isLoading = true); when (val r = friendRepository.getFriends()) { is Result.Success -> _uiState.value = _uiState.value.copy(isLoading = false, friends = r.data.friends ?: emptyList()); is Result.Error -> _uiState.value = _uiState.value.copy(isLoading = false, error = r.message); else -> {} } } }
    fun searchUsers(query: String) { if (query.isBlank()) { _uiState.value = _uiState.value.copy(searchResults = emptyList(), isSearching = false); return }; viewModelScope.launch { _uiState.value = _uiState.value.copy(isSearching = true); when (val r = friendRepository.searchUsers(query)) { is Result.Success -> _uiState.value = _uiState.value.copy(isSearching = false, searchResults = r.data.results ?: emptyList()); else -> _uiState.value = _uiState.value.copy(isSearching = false) } } }
    fun sendFriendRequest(username: String) { viewModelScope.launch { when (friendRepository.sendFriendRequest(username)) { is Result.Success -> _uiState.value = _uiState.value.copy(actionMessage = "Request sent"); else -> {} } } }
    fun acceptFriendRequest(senderName: String) { viewModelScope.launch { when (friendRepository.acceptFriendRequest(senderName)) { is Result.Success -> { _uiState.value = _uiState.value.copy(actionMessage = "Friend added"); loadFriends() }; else -> {} } } }
    fun rejectFriendRequest(senderName: String) { viewModelScope.launch { when (friendRepository.rejectFriendRequest(senderName)) { is Result.Success -> loadFriends(); else -> {} } } }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendsScreen(uiState: FriendsUiState, onSearch: (String) -> Unit, onSendRequest: (String) -> Unit, onAcceptRequest: (String) -> Unit, onRejectRequest: (String) -> Unit, onRefresh: () -> Unit) {
    var searchQuery by remember { mutableStateOf("") }
    Scaffold(topBar = { TopAppBar(title = { Text("Friends", fontWeight = FontWeight.Bold) }) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            OutlinedTextField(searchQuery, { searchQuery = it; onSearch(it) }, placeholder = { Text("Search users...") }, leadingIcon = { Icon(Icons.Filled.Search, null) }, singleLine = true, modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp), shape = RoundedCornerShape(16.dp))
            if (uiState.isLoading) ShimmerList()
            else LazyColumn(Modifier.fillMaxSize().padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (searchQuery.isNotBlank() && uiState.searchResults.isNotEmpty()) { item { Text("Search Results", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(vertical = 8.dp)) }; items(uiState.searchResults) { FriendItem(it, onSendRequest = { onSendRequest(it.username) }) } }
                if (uiState.friends.isNotEmpty()) { item { Text("Your Friends", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(vertical = 8.dp)) }; items(uiState.friends) { FriendItem(it) } }
                if (uiState.friends.isEmpty() && searchQuery.isBlank()) item { EmptyState(Icons.Outlined.People, "No friends yet", "Search for users to add friends") }
                item { Spacer(Modifier.height(80.dp)) }
            }
        }
    }
}
