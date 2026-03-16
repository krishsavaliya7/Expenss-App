package com.splitsmart.app.data.repository

import com.splitsmart.app.data.api.SplitSmartApi
import com.splitsmart.app.data.api.models.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GroupRepository @Inject constructor(private val api: SplitSmartApi) {
    suspend fun getGroups(): Result<GroupListResponse> = try {
        val r = api.getGroups(); if (r.isSuccessful) Result.Success(r.body()!!) else Result.Error(r.body()?.error ?: "Failed", r.code())
    } catch (e: Exception) { Result.Error(e.message ?: "Network error") }

    suspend fun getGroup(groupId: Int): Result<GroupDetailResponse> = try {
        val r = api.getGroup(groupId); if (r.isSuccessful) Result.Success(r.body()!!) else Result.Error("Failed", r.code())
    } catch (e: Exception) { Result.Error(e.message ?: "Network error") }

    suspend fun createGroup(name: String, description: String?, currency: String, members: List<String>?): Result<CreateGroupResponse> = try {
        val r = api.createGroup(CreateGroupRequest(name, description, currency, members)); if (r.isSuccessful) Result.Success(r.body()!!) else Result.Error(r.body()?.error ?: "Failed", r.code())
    } catch (e: Exception) { Result.Error(e.message ?: "Network error") }

    suspend fun getMemberSuggestions(groupId: Int): Result<MemberSuggestionsResponse> = try {
        val r = api.getMemberSuggestions(groupId); if (r.isSuccessful) Result.Success(r.body()!!) else Result.Error("Failed", r.code())
    } catch (e: Exception) { Result.Error(e.message ?: "Network error") }

    suspend fun addMember(groupId: Int, username: String): Result<CreateGroupResponse> = try {
        val r = api.addGroupMember(groupId, AddMemberRequest(username)); if (r.isSuccessful) Result.Success(r.body()!!) else Result.Error(r.body()?.error ?: "Failed", r.code())
    } catch (e: Exception) { Result.Error(e.message ?: "Network error") }

    suspend fun getExpenses(groupId: Int): Result<ExpenseListResponse> = try {
        val r = api.getExpenses(groupId); if (r.isSuccessful) Result.Success(r.body()!!) else Result.Error("Failed", r.code())
    } catch (e: Exception) { Result.Error(e.message ?: "Network error") }

    suspend fun createExpense(groupId: Int, request: CreateExpenseRequest): Result<CreateExpenseResponse> = try {
        val r = api.createExpense(groupId, request); if (r.isSuccessful) Result.Success(r.body()!!) else Result.Error(r.body()?.error ?: "Failed", r.code())
    } catch (e: Exception) { Result.Error(e.message ?: "Network error") }

    suspend fun deleteExpense(groupId: Int, expenseId: Int): Result<DeleteExpenseResponse> = try {
        val r = api.deleteExpense(groupId, expenseId); if (r.isSuccessful) Result.Success(r.body()!!) else Result.Error(r.body()?.error ?: "Failed", r.code())
    } catch (e: Exception) { Result.Error(e.message ?: "Network error") }

    suspend fun getBalances(groupId: Int): Result<BalancesResponse> = try {
        val r = api.getBalances(groupId); if (r.isSuccessful) Result.Success(r.body()!!) else Result.Error("Failed", r.code())
    } catch (e: Exception) { Result.Error(e.message ?: "Network error") }

    suspend fun getSettlements(groupId: Int): Result<SettlementResponse> = try {
        val r = api.getSettlements(groupId); if (r.isSuccessful) Result.Success(r.body()!!) else Result.Error("Failed", r.code())
    } catch (e: Exception) { Result.Error(e.message ?: "Network error") }

    suspend fun requestCashSettlement(groupId: Int, toUser: String, amount: Double): Result<SettlementActionResponse> = try {
        val r = api.requestCashSettlement(groupId, CashSettlementRequest(toUser, amount)); if (r.isSuccessful) Result.Success(r.body()!!) else Result.Error(r.body()?.error ?: "Failed", r.code())
    } catch (e: Exception) { Result.Error(e.message ?: "Network error") }

    suspend fun approveCashSettlement(groupId: Int, settlementId: Int): Result<SettlementActionResponse> = try {
        val r = api.approveCashSettlement(groupId, settlementId); if (r.isSuccessful) Result.Success(r.body()!!) else Result.Error(r.body()?.error ?: "Failed", r.code())
    } catch (e: Exception) { Result.Error(e.message ?: "Network error") }

    suspend fun initiateUpiSettlement(groupId: Int, toUser: String, amount: Double): Result<SettlementActionResponse> = try {
        val r = api.initiateUpiSettlement(groupId, UpiSettlementRequest(toUser, amount)); if (r.isSuccessful) Result.Success(r.body()!!) else Result.Error(r.body()?.error ?: "Failed", r.code())
    } catch (e: Exception) { Result.Error(e.message ?: "Network error") }

    suspend fun confirmUpiSettlement(groupId: Int, settlementId: Int): Result<SettlementActionResponse> = try {
        val r = api.confirmUpiSettlement(groupId, settlementId); if (r.isSuccessful) Result.Success(r.body()!!) else Result.Error(r.body()?.error ?: "Failed", r.code())
    } catch (e: Exception) { Result.Error(e.message ?: "Network error") }

    suspend fun getTransactions(groupId: Int): Result<TransactionsResponse> = try {
        val r = api.getTransactions(groupId); if (r.isSuccessful) Result.Success(r.body()!!) else Result.Error("Failed", r.code())
    } catch (e: Exception) { Result.Error(e.message ?: "Network error") }

    suspend fun joinGroup(token: String): Result<JoinGroupResponse> = try {
        val r = api.joinGroupViaInvite(token); if (r.isSuccessful) Result.Success(r.body()!!) else Result.Error(r.body()?.error ?: "Failed", r.code())
    } catch (e: Exception) { Result.Error(e.message ?: "Network error") }
}
