package com.splitsmart.app.ui.screens.groups

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.splitsmart.app.data.api.models.*
import com.splitsmart.app.data.repository.GroupRepository
import com.splitsmart.app.data.repository.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GroupUiState(val isLoading: Boolean = false, val groups: List<GroupData> = emptyList(), val currentGroup: GroupData? = null, val members: List<MemberData> = emptyList(), val expenses: List<ExpenseData> = emptyList(), val balances: List<BalanceData> = emptyList(), val settlements: List<SettlementData> = emptyList(), val createdGroupId: Int? = null, val expenseCreated: Boolean = false, val error: String? = null, val actionMessage: String? = null)

@HiltViewModel
class GroupViewModel @Inject constructor(private val groupRepository: GroupRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(GroupUiState()); val uiState: StateFlow<GroupUiState> = _uiState.asStateFlow()
    init { loadGroups() }

    fun loadGroups() { viewModelScope.launch { _uiState.value = _uiState.value.copy(isLoading = true); when (val r = groupRepository.getGroups()) { is Result.Success -> _uiState.value = _uiState.value.copy(isLoading = false, groups = r.data.groups ?: emptyList()); is Result.Error -> _uiState.value = _uiState.value.copy(isLoading = false, error = r.message); else -> {} } } }

    fun loadGroupDetail(groupId: Int) { viewModelScope.launch {
        _uiState.value = _uiState.value.copy(isLoading = true)
        when (val r = groupRepository.getGroup(groupId)) { is Result.Success -> _uiState.value = _uiState.value.copy(currentGroup = r.data.group, members = r.data.members ?: emptyList()); else -> {} }
        when (val r = groupRepository.getExpenses(groupId)) { is Result.Success -> _uiState.value = _uiState.value.copy(expenses = r.data.expenses ?: emptyList()); else -> {} }
        when (val r = groupRepository.getBalances(groupId)) { is Result.Success -> _uiState.value = _uiState.value.copy(balances = r.data.balances ?: emptyList()); else -> {} }
        _uiState.value = _uiState.value.copy(isLoading = false)
    }}

    fun createGroup(name: String, desc: String?, currency: String, members: List<String>?) { viewModelScope.launch { _uiState.value = _uiState.value.copy(isLoading = true, error = null); when (val r = groupRepository.createGroup(name, desc, currency, members)) { is Result.Success -> _uiState.value = _uiState.value.copy(isLoading = false, createdGroupId = r.data.groupId); is Result.Error -> _uiState.value = _uiState.value.copy(isLoading = false, error = r.message); else -> {} } } }
    fun createExpense(groupId: Int, request: CreateExpenseRequest) { viewModelScope.launch { _uiState.value = _uiState.value.copy(isLoading = true, error = null); when (val r = groupRepository.createExpense(groupId, request)) { is Result.Success -> _uiState.value = _uiState.value.copy(isLoading = false, expenseCreated = true); is Result.Error -> _uiState.value = _uiState.value.copy(isLoading = false, error = r.message); else -> {} } } }
    fun deleteExpense(groupId: Int, expenseId: Int) { viewModelScope.launch { when (groupRepository.deleteExpense(groupId, expenseId)) { is Result.Success -> loadGroupDetail(groupId); else -> {} } } }
    fun loadSettlements(groupId: Int) { viewModelScope.launch { _uiState.value = _uiState.value.copy(isLoading = true); when (val r = groupRepository.getSettlements(groupId)) { is Result.Success -> _uiState.value = _uiState.value.copy(isLoading = false, settlements = r.data.settlements ?: emptyList()); is Result.Error -> _uiState.value = _uiState.value.copy(isLoading = false, error = r.message); else -> {} } } }
    fun requestCashSettlement(groupId: Int, toUser: String, amount: Double) { viewModelScope.launch { when (val r = groupRepository.requestCashSettlement(groupId, toUser, amount)) { is Result.Success -> { _uiState.value = _uiState.value.copy(actionMessage = "Cash settlement requested"); loadSettlements(groupId) }; is Result.Error -> _uiState.value = _uiState.value.copy(error = r.message); else -> {} } } }
    fun initiateUpiSettlement(groupId: Int, toUser: String, amount: Double) { viewModelScope.launch { when (val r = groupRepository.initiateUpiSettlement(groupId, toUser, amount)) { is Result.Success -> { _uiState.value = _uiState.value.copy(actionMessage = "UPI settlement initiated"); loadSettlements(groupId) }; is Result.Error -> _uiState.value = _uiState.value.copy(error = r.message); else -> {} } } }
    fun clearCreatedGroupId() { _uiState.value = _uiState.value.copy(createdGroupId = null) }
    fun clearExpenseCreated() { _uiState.value = _uiState.value.copy(expenseCreated = false) }
}
